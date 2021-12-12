package com.richarddewan.paging3_todo.data.repository.rx

import androidx.paging.PagingData
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import io.reactivex.rxjava3.core.Flowable

interface TaskRxRemoteMediatorRepository {
    fun getTaskList():Flowable<PagingData<TaskEntity>>
}