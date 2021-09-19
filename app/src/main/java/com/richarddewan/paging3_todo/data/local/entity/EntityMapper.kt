package com.richarddewan.paging3_todo.data.local.entity

interface EntityMapper<Model,Entity> {
    abstract fun mapToEntity(model:List<Model>):List<Entity>
}