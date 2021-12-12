package com.richarddewan.paging3_todo.data.repository.rx

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.richarddewan.paging3_todo.data.local.db.DbService
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import com.richarddewan.paging3_todo.data.repository.flow.TaskFlowRemoteMediatorRepository
import com.richarddewan.paging3_todo.data.repository.paging.TaskFlowRemoteMediator
import com.richarddewan.paging3_todo.data.repository.paging.TaskRxRemoteMediator
import io.reactivex.rxjava3.core.Flowable

class TaskRxRemoteMediatorRepositoryImpl @ExperimentalPagingApi constructor(
    private val dbService: DbService,
    private val rxRemoteMediator: TaskRxRemoteMediator
):TaskRxRemoteMediatorRepository{

    @ExperimentalPagingApi
    override fun getTaskList(): Flowable<PagingData<TaskEntity>> {
        return Pager(
            config = defaultPagingConfig(),
            remoteMediator = rxRemoteMediator,
            pagingSourceFactory = {dbService.taskRxDao().getTasks()}
        ).flowable
    }

    private fun defaultPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 10,
            prefetchDistance = 10,
            enablePlaceholders = false,
            initialLoadSize = 20,
            maxSize = 30 //pageSize + (2 * prefetchDistance)`) of the most recent load.
        )
    }
}