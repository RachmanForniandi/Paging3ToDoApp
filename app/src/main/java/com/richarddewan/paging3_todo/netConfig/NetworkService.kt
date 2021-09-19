package com.richarddewan.paging3_todo.netConfig

import com.richarddewan.paging3_todo.model.TaskResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NetworkService {

    @Headers(CoreEndPoints.HEADER_ACCEPT)
    @GET(CoreEndPoints.URL_ALL_TASK)
    suspend fun getTaskListFlow(@Query("page")pageNumber:Int):TaskResponse

    @Headers(CoreEndPoints.HEADER_ACCEPT)
    @GET(CoreEndPoints.URL_ALL_TASK)
    fun getTaskListRx(@Query("page")pageNumber:Int):Single<TaskResponse>
}