package com.richarddewan.paging3_todo.ui.flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.richarddewan.paging3_todo.data.repository.flow.TaskFlowRepositoryImpl
import com.richarddewan.paging3_todo.model.DataPaging
import kotlinx.coroutines.flow.Flow

class FlowViewModel(private val repositoryImpl: TaskFlowRepositoryImpl):ViewModel() {

    fun getTaskListPaging(): Flow<PagingData<DataPaging.Task>> =
        repositoryImpl.getTaskListPaging()
            .cachedIn(viewModelScope)
}