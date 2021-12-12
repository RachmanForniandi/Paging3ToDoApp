package com.richarddewan.paging3_todo.data.repository.paging

import androidx.compose.runtime.key
import androidx.paging.*
import androidx.paging.rxjava3.RxRemoteMediator
import com.richarddewan.paging3_todo.data.local.db.DbService
import com.richarddewan.paging3_todo.data.local.entity.EntityMapper
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import com.richarddewan.paging3_todo.data.local.entity.TaskKeyEntity
import com.richarddewan.paging3_todo.model.DataPaging
import com.richarddewan.paging3_todo.model.TaskResponse
import com.richarddewan.paging3_todo.model.TaskResponseMapper
import com.richarddewan.paging3_todo.netConfig.NetworkService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.io.InvalidObjectException

@ExperimentalPagingApi
class TaskRxRemoteMediator(private val networkService: NetworkService,
                           private val dbService: DbService
): RxRemoteMediator<Int, TaskEntity>(),
    TaskResponseMapper<TaskResponse, DataPaging>,
    EntityMapper<DataPaging.Task, TaskEntity> {
    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, TaskEntity>
    ): Single<MediatorResult> {
        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .map { type->
                when(type){
                    /**
                     *[pagingData] content being refreshed, which can be result of [pagingSource]
                     *invalidation, refresh that may contain content updates, or initial load
                     */
                    LoadType.REFRESH->{

                        val keys = getKeyForTheClosestToCurrentItemPosition(state)
                        keys?.nextKey?.minus(1)?:1
                    }
                    /**
                     * Load at the start of a [PagingData].
                     */
                    LoadType.PREPEND->{
                        val keys = getKeyForTheFirstItem(state)?: throw InvalidObjectException("First Item is empty")

                        keys.previousKey ?: INVALID_PAGE
                    }
                    LoadType.APPEND->{
                        /*
                        *load at the end of [pagingdata]
                        * */

                        val keys = getKeyForTheLastItem(state)?: throw InvalidObjectException("First Item is empty")

                        keys.nextKey ?: INVALID_PAGE
                    }
                }
            }
            .flatMap { page->
                if (page == INVALID_PAGE){
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                }else{
                    networkService.getTaskListRx(pageNumber = page)
                        .map { mapFromResponse(it) }
                        .map { insertToDb(page,loadType,it) }
                        .map<MediatorResult> {
                            MediatorResult.Success(endOfPaginationReached = it.endPage)
                        }.onErrorReturn {
                            MediatorResult.Error(it)
                        }
                }
            }.onErrorReturn{ MediatorResult.Error(it)}
    }

    private fun insertToDb(page: Int, loadType: LoadType, data: DataPaging):DataPaging {
        try {
            if (loadType == LoadType.REFRESH) {
                dbService.taskKeyRxDao().clearTaskKeys()
                dbService.taskRxDao().clearTasks()
            }
            val previousKey = if (page ==1) null else page -1
            val nextKey = if (data.endPage) null else page + 1
            val keys = data.tasks.map {
                TaskKeyEntity(taskId = it.id.toLong(),previousKey, nextKey)

            }

            //insert to local db
            dbService.taskRxDao().insertMany(mapToEntity(data.tasks))
            dbService.taskKeyRxDao().insertMany(keys)
        }catch (e:Exception){
            Timber.e(e)
        }
        return data
    }

    private  fun getKeyForTheFirstItem(state: PagingState<Int, TaskEntity>): TaskKeyEntity? {
        return state.pages.firstOrNull(){
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { taskEntity ->
            dbService.taskKeyRxDao().getTaskKeyByTaskId(taskEntity.taskId.toInt())
        }
    }

    private fun getKeyForTheLastItem(state: PagingState<Int, TaskEntity>): TaskKeyEntity?{
        return state.pages.lastOrNull(){
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { taskEntity ->
            dbService.taskKeyRxDao().getTaskKeyByTaskId(taskEntity.taskId.toInt())
        }
    }

    private fun getKeyForTheClosestToCurrentItemPosition(state: PagingState<Int, TaskEntity>): TaskKeyEntity? {
        return state.anchorPosition?.let { position->
            state.closestItemToPosition(position)?.taskId?.let {
                dbService.taskKeyRxDao().getTaskKeyByTaskId(it.toInt())
            }

        }
    }

    override fun mapToEntity(model: List<DataPaging.Task>): List<TaskEntity> {
        return model.map {
            TaskEntity(
                taskId = it.id.toLong(),
                userId = it.userId,
                title = it.title,
                body = it.body,
                note = it.note,
                status = it.status
            )
        }
    }

    override fun mapFromResponse(response: TaskResponse): DataPaging {
        return with(response){
            DataPaging(
                totalPage = lastPage,
                currentPage = currentPage,
                tasks = task.map {
                    DataPaging.Task(
                        id = it.id,
                        userId = it.userId,
                        title = it.title,
                        body = it.body,
                        note = it.note,
                        status = it.status,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt
                    )
                }
            )
        }
    }

    companion object{
        const val INVALID_PAGE = -1
    }
}