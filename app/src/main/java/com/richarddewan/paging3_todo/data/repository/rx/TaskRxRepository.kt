package com.richarddewan.paging3_todo.data.repository.rx

import androidx.paging.PagingData
import com.richarddewan.paging3_todo.model.DataPaging
import io.reactivex.rxjava3.core.Flowable

interface TaskRxRepository {
    fun getTaskListPaging(): Flowable<PagingData<DataPaging.Task>>
}