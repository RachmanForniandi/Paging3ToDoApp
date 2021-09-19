package com.richarddewan.paging3_todo.data.local.db.rxdoa

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import com.richarddewan.paging3_todo.data.local.entity.TaskKeyEntity

interface TaskKeyRxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(keys: List<TaskKeyEntity>)

    @Query("SELECT * FROM task_keys WHERE taskId =:taskId")
    fun getTaskKeyByTaskId(taskId:Int): TaskEntity

    @Query("DELETE FROM task_keys")
    suspend fun clearTaskKeys()
}