package com.richarddewan.paging3_todo.ui.model

import com.richarddewan.paging3_todo.data.local.entity.TaskEntity

sealed class UiModel{
    data class TaskItem(val task:TaskEntity):UiModel()
    data class SeparatorItem(val status:String):UiModel()
}
