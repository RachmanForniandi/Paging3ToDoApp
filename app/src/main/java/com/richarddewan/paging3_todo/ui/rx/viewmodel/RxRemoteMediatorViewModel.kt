package com.richarddewan.paging3_todo.ui.rx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.rxjava3.cachedIn
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import com.richarddewan.paging3_todo.data.repository.rx.TaskRxRemoteMediatorRepositoryImpl
import com.richarddewan.paging3_todo.model.DataPaging
import com.richarddewan.paging3_todo.ui.model.UiModel
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RxRemoteMediatorViewModel(
    private val repository: TaskRxRemoteMediatorRepositoryImpl
    ):ViewModel() {

    @ExperimentalPagingApi
    fun getRxTaskList(): Flowable<PagingData<TaskEntity>> =
        repository.getTaskList()
            .cachedIn(viewModelScope)

    @ExperimentalPagingApi
    fun getTaskListUiModel(): Flowable<PagingData<UiModel>> {

        return repository.getTaskList()
            .map {
                it.map { UiModel.TaskItem(it) }
            }
            .map {
                it.insertSeparators { before, after ->
                    if (after == null) {
                        return@insertSeparators null
                    }

                    val alpha = after.task.status.trim().take(1)

                    if (before == null) {
                        //er are at the begenning of the list
                        return@insertSeparators UiModel
                            .SeparatorItem("$alpha : ${after.task.status}")
                    }

                    if (after.task.status == "STARTED"
                        || after.task.status == "PENDING"
                        || after.task.status == "COMPLETED"
                        || after.task.status == "bbb1"
                        || after.task.status == "bbb"
                        || after.task.status == "Started"
                    ) {
                        UiModel.SeparatorItem("\"$alpha : ${after.task.status}")
                    } else {
                        null
                    }
                }
            }.cachedIn(viewModelScope)
    }
}