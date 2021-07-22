package com.richarddewan.paging3_todo.model

interface TaskResponseMapper<Response,Model> {

    abstract fun mapFromResponse(response: Response):Model
}