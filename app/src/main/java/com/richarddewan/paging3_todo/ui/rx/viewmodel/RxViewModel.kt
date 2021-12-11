package com.richarddewan.paging3_todo.ui.rx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.richarddewan.paging3_todo.data.repository.rx.TaskRxRepositoryImpl
import com.richarddewan.paging3_todo.model.DataPaging
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi

class RxViewModel(private val repository: TaskRxRepositoryImpl): ViewModel() {

    @ExperimentalCoroutinesApi
    fun getRxTaskList(): Flowable<PagingData<DataPaging.Task>> =
        repository.getTaskListPaging()
            .cachedIn(viewModelScope)
}