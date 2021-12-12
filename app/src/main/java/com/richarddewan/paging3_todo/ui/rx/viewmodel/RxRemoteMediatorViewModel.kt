package com.richarddewan.paging3_todo.ui.rx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import com.richarddewan.paging3_todo.data.repository.rx.TaskRxRemoteMediatorRepositoryImpl
import com.richarddewan.paging3_todo.model.DataPaging
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi

class RxRemoteMediatorViewModel(
    private val repository: TaskRxRemoteMediatorRepositoryImpl
    ):ViewModel() {

    @ExperimentalPagingApi
    fun getRxTaskList(): Flowable<PagingData<TaskEntity>> =
        repository.getTaskList()
            .cachedIn(viewModelScope)
}