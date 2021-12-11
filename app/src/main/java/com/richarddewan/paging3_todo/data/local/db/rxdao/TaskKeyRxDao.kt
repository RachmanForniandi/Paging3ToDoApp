package com.richarddewan.paging3_todo.data.local.db.rxdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import com.richarddewan.paging3_todo.data.local.entity.TaskKeyEntity

@Dao
interface TaskKeyRxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMany(keys: List<TaskKeyEntity>)

    @Query("SELECT * FROM task_keys WHERE taskId =:taskId")
    fun getTaskKeyByTaskId(taskId:Int): TaskKeyEntity

    @Query("DELETE FROM task_keys")
    fun clearTaskKeys()
}