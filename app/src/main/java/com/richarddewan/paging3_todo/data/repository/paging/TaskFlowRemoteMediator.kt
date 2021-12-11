package com.richarddewan.paging3_todo.data.repository.paging

import androidx.compose.runtime.key
import androidx.paging.*
import androidx.room.withTransaction
import com.richarddewan.paging3_todo.data.local.db.DbService
import com.richarddewan.paging3_todo.data.local.entity.EntityMapper
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import com.richarddewan.paging3_todo.data.local.entity.TaskKeyEntity
import com.richarddewan.paging3_todo.model.DataPaging
import com.richarddewan.paging3_todo.model.TaskResponse
import com.richarddewan.paging3_todo.model.TaskResponseMapper
import com.richarddewan.paging3_todo.netConfig.NetworkService
import java.io.InvalidObjectException

@ExperimentalPagingApi
class TaskFlowRemoteMediator(private val networkService: NetworkService,
                             private val dbService: DbService
                             ):RemoteMediator<Int,TaskEntity>(), TaskResponseMapper<TaskResponse,DataPaging>,
    EntityMapper<DataPaging.Task,TaskEntity> {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TaskEntity>
    ): MediatorResult {
        val page = when(loadType){

            LoadType.REFRESH->{
                /**
                *[pagingData] content being refreshed, which can be result of [pagingSource]
                 *invalidation, refresh that may contain content updates, or initial load
                */
                val keys = getKeyForTheClosestToCurrentItemPosition(state)
                keys?.nextKey?.minus(1)?:1
            }
            /**
             * Load at the start of a [PagingData].
             */
            LoadType.PREPEND->{
                val keys = getKeyForTheFirstItem(state)?: return MediatorResult.Error(InvalidObjectException("First Item is empty"))

                val previousKey = keys.previousKey ?:return MediatorResult.Success(endOfPaginationReached = true)

                previousKey
            }
            LoadType.APPEND->{
                /*
                *load at the end of [pagingdata]
                * */

                val keys = getKeyForTheLastItem(state)?: return MediatorResult.Error(InvalidObjectException("Last Item is empty"))

                val nextKey = keys.nextKey
                    ?: return  MediatorResult.Success(endOfPaginationReached = true)
                nextKey
            }
        }

        try {
            val responseNetwork = networkService.getTaskListFlow(pageNumber = page)
            val data = mapFromResponse(responseNetwork)

            dbService.withTransaction {
                if (loadType == LoadType.REFRESH){
                    dbService.taskFlowDao().clearTasks()
                    dbService.taskKeyFlowDao().clearKeys()
                }

                val previousKey = if (page ==1) null else page -1
                val nextKey = if (data.endPage) null else page + 1
                val keys = data.tasks.map {
                    TaskKeyEntity(taskId = it.id.toLong(),previousKey, nextKey)

                }

                //insert to local db
                dbService.taskKeyFlowDao().insertMany(keys)
                dbService.taskFlowDao().insertMany(mapToEntity(data.tasks
                ))
            }
            return MediatorResult.Success(endOfPaginationReached = data.endPage)
        }catch (e: Exception){
           return MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyForTheFirstItem(state: PagingState<Int, TaskEntity>):TaskKeyEntity? {
        return state.pages.firstOrNull(){
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { taskEntity ->
            dbService.taskKeyFlowDao().getTaskKeyByTaskId(taskEntity.taskId.toInt())
        }
    }

    private suspend fun getKeyForTheLastItem(state: PagingState<Int, TaskEntity>):TaskKeyEntity?{
        return state.pages.lastOrNull(){
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { taskEntity ->
            dbService.taskKeyFlowDao().getTaskKeyByTaskId(taskEntity.taskId.toInt())
        }
    }

    private suspend fun getKeyForTheClosestToCurrentItemPosition(state: PagingState<Int, TaskEntity>): TaskKeyEntity? {
        return state.anchorPosition?.let { position->
            state.closestItemToPosition(position)?.taskId?.let {
                dbService.taskKeyFlowDao().getTaskKeyByTaskId(it.toInt())
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


}

