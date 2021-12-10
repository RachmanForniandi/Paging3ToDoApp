package com.richarddewan.paging3_todo.data.repository.flow

import androidx.paging.PagingData
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskFlowRemoteMediatorRepository {

    fun getTaskList(): Flow<PagingData<TaskEntity>>
}