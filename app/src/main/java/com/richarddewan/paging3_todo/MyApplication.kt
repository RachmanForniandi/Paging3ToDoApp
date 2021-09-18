package com.richarddewan.paging3_todo

import android.app.Application
import com.richarddewan.paging3_todo.netConfig.NetworkConfig
import com.richarddewan.paging3_todo.netConfig.NetworkService

class MyApplication: Application()  {
    lateinit var networkService:NetworkService
    private val baseUrl:String ="https://freeapi.rdewan.dev/"

    override fun onCreate() {
        super.onCreate()
        networkService = NetworkConfig.initiate(baseUrl,cacheDir,1024 * 1024 *10)
    }
}