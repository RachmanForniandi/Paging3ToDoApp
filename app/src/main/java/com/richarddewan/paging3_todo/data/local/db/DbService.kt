package com.richarddewan.paging3_todo.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.richarddewan.paging3_todo.data.local.db.flowdoa.TaskFlowDao
import com.richarddewan.paging3_todo.data.local.db.flowdoa.TaskKeyFlowDao
import com.richarddewan.paging3_todo.data.local.db.rxdoa.TaskKeyRxDao
import com.richarddewan.paging3_todo.data.local.db.rxdoa.TaskRxDao
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import com.richarddewan.paging3_todo.data.local.entity.TaskKeyEntity

@Database(
    entities = [TaskEntity::class,
    TaskKeyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DbService:RoomDatabase() {

    abstract fun taskFlowDao():TaskFlowDao
    abstract fun taskKeyFlowDao():TaskKeyFlowDao

    abstract fun taskRxDao():TaskRxDao
    abstract fun taskKeyRxDao():TaskKeyRxDao

    companion object{
        @Volatile
        private var INSTANCE:DbService?= null

        fun getInstance(context:Context):DbService=
            INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDb(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDb(context:Context)=
            Room.databaseBuilder(context.applicationContext,
                DbService::class.java,"paging.db")
                .build()
    }
}