package com.richarddewan.paging3_todo.data.repository.flow

import androidx.paging.PagingData
import com.richarddewan.paging3_todo.model.DataPaging
import kotlinx.coroutines.flow.Flow

interface TaskFlowRepository {
    fun getTaskListPaging(): Flow<PagingData<DataPaging.Task>>
}