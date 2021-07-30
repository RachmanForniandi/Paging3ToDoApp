package com.richarddewan.paging3_todo.data.repository.flow

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.richarddewan.paging3_todo.data.repository.paging.TaskFlowPagingSource
import com.richarddewan.paging3_todo.model.DataPaging
import kotlinx.coroutines.flow.Flow

class TaskFlowRepositoryImpl(private val pagingSource: TaskFlowPagingSource):TaskFlowRepository {

    override fun getTaskListPaging(): Flow<PagingData<DataPaging.Task>> {
        return Pager(defaultPagingConfig(),pagingSourceFactory = {pagingSource}
        ).flow
    }

    private fun defaultPagingConfig():PagingConfig{
        return PagingConfig(
            pageSize = 10,
            prefetchDistance = 10,
            enablePlaceholders = false,
            initialLoadSize = 30,
            maxSize = 30

        )
    }
}