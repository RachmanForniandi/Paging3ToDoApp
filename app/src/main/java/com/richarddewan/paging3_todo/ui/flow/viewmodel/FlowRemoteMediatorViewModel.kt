package com.richarddewan.paging3_todo.ui.flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import com.richarddewan.paging3_todo.data.repository.flow.TaskFlowRemoteMediatorRepositoryImpl
import kotlinx.coroutines.flow.Flow

class FlowRemoteMediatorViewModel(
    private val repositoryImpl:TaskFlowRemoteMediatorRepositoryImpl
) : ViewModel(){

    @ExperimentalPagingApi
    fun getTaskList(): Flow<PagingData<TaskEntity>> {
        return repositoryImpl.getTaskList().cachedIn(viewModelScope)
    }



}