package com.richarddewan.paging3_todo.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "task_keys")
data class TaskKeyEntity(
    @PrimaryKey
    val taskId:Long,
    val previousKey:Int?,
    val nextKey:Int?
)
