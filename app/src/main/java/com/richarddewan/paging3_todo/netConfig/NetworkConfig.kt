package com.richarddewan.paging3_todo.netConfig

import com.richarddewan.paging3_todo.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object NetworkConfig {

    private const val NETWORK_CALL_TIME_OUT = 60L
    private lateinit var okHttpClient: OkHttpClient

    fun initiate(baseUrl:String,cacheDir: File,cacheSize:Long):NetworkService{
         okHttpClient = OkHttpClient.Builder()
             .cache(Cache(cacheDir,cacheSize))
             .readTimeout(NETWORK_CALL_TIME_OUT, TimeUnit.SECONDS)
             .writeTimeout(NETWORK_CALL_TIME_OUT, TimeUnit.SECONDS)
             .addInterceptor(HttpLoggingInterceptor().apply {
                 level = if (BuildConfig.DEBUG){
                     HttpLoggingInterceptor.Level.BODY
                 }else{
                     HttpLoggingInterceptor.Level.NONE
                 }
             }).build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }
}