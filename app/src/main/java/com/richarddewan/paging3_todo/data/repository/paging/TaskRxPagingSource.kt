package com.richarddewan.paging3_todo.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.richarddewan.paging3_todo.model.DataPaging
import com.richarddewan.paging3_todo.model.TaskResponse
import com.richarddewan.paging3_todo.model.TaskResponseMapper
import com.richarddewan.paging3_todo.netConfig.NetworkService
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class TaskRxPagingSource (private val networkService: NetworkService):
    RxPagingSource<Int, DataPaging.Task>()
    , TaskResponseMapper<TaskResponse, DataPaging> {
    override fun getRefreshKey(state: PagingState<Int, DataPaging.Task>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, DataPaging.Task>> {
        val currentPage = params.key ?:1

        return networkService.getTaskListRx(currentPage)
            .subscribeOn(Schedulers.io())
            .map {
                mapFromResponse(it)
            }.map {
                loadResult(data = it,currentPage = currentPage)
            }.onErrorReturn { LoadResult.Error(it) }
    }

    private fun loadResult(data:DataPaging, currentPage:Int):LoadResult<Int,DataPaging.Task> =
        LoadResult.Page(
            data = data.tasks,
            prevKey = if (currentPage==1) null else currentPage -1,
            nextKey = if (data.endPage) null else currentPage +1
        )

    override fun mapFromResponse(response: TaskResponse): DataPaging {
        return with(response) {
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