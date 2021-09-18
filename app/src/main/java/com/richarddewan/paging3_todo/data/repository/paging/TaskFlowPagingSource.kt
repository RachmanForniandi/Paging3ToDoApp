package com.richarddewan.paging3_todo.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.richarddewan.paging3_todo.model.DataPaging
import com.richarddewan.paging3_todo.model.TaskResponse
import com.richarddewan.paging3_todo.model.TaskResponseMapper
import com.richarddewan.paging3_todo.netConfig.NetworkService

class TaskFlowPagingSource(private val networkService: NetworkService):PagingSource<Int, DataPaging.Task>()
    ,TaskResponseMapper<TaskResponse,DataPaging> {

    override fun getRefreshKey(state: PagingState<Int, DataPaging.Task>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataPaging.Task> {
        val currentPage = params.key ?:1

        return try {
            networkService.getTaskListFlow(currentPage)
                .run {
                    val data = mapFromResponse(this)

                    return LoadResult.Page(
                        data = data.tasks,
                        prevKey = if (currentPage ==1) null else currentPage -1,
                        nextKey = if (data.endPage) null else currentPage +1
                    )
                }
        }catch (e:Exception){
            LoadResult.Error(e)
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