package com.richarddewan.paging3_todo.util

import android.app.Application
import com.richarddewan.paging3_todo.data.local.db.DbService
import com.richarddewan.paging3_todo.netConfig.NetworkConfig
import com.richarddewan.paging3_todo.netConfig.NetworkService

class MyApplication: Application()  {
    lateinit var networkService:NetworkService
    lateinit var dbService: DbService
    private val baseUrl:String ="https://freeapi.rdewan.dev/"

    override fun onCreate() {
        super.onCreate()
        networkService = NetworkConfig.initiate(baseUrl,cacheDir,1024 * 1024 *10)
        dbService = DbService.getInstance(applicationContext)
    }
}