package com.richarddewan.paging3_todo.data.repository.rx

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.richarddewan.paging3_todo.data.repository.paging.TaskRxPagingSource
import com.richarddewan.paging3_todo.model.DataPaging
import io.reactivex.rxjava3.core.Flowable

class TaskRxRepositoryImpl (private val pagingSource:TaskRxPagingSource):TaskRxRepository{

    override fun getTaskListPaging(): Flowable<PagingData<DataPaging.Task>> {
        return Pager(
            config = defaultPagingConfig(),
            pagingSourceFactory = {pagingSource}
        ).flowable
    }

    private fun defaultPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 10,
            prefetchDistance = 10,
            enablePlaceholders = false,
            initialLoadSize = 30,
            maxSize = 30

        )
    }

}