package com.richarddewan.paging3_todo.data.repository.flow

import androidx.paging.PagingData
import com.richarddewan.paging3_todo.data.local.db.DbService
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskFlowRemoteMediatorRepositoryImpl(
    private val dbService:DbService,
private val remoteMediatorRepositoryImpl: TaskFlowRemoteMediatorRepositoryImpl):TaskFlowRemoteMediatorRepository {

    override fun getTaskList(): Flow<PagingData<TaskEntity>> {
        TODO("Not yet implemented")
    }

}