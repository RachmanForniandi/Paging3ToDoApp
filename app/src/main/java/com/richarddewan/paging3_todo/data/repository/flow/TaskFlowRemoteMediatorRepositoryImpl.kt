package com.richarddewan.paging3_todo.data.repository.flow

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.richarddewan.paging3_todo.data.local.db.DbService
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import com.richarddewan.paging3_todo.data.repository.paging.TaskFlowRemoteMediator
import kotlinx.coroutines.flow.Flow

class TaskFlowRemoteMediatorRepositoryImpl @ExperimentalPagingApi constructor(
    private val dbService:DbService,
private val taskFlowRemoteMediator: TaskFlowRemoteMediator):TaskFlowRemoteMediatorRepository {


    @ExperimentalPagingApi
    override fun getTaskList(): Flow<PagingData<TaskEntity>> {
        return Pager(
            config = defaultPagingConfig(),
            remoteMediator = taskFlowRemoteMediator,
            pagingSourceFactory = {dbService.taskFlowDao().getTasks()}
        ).flow
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