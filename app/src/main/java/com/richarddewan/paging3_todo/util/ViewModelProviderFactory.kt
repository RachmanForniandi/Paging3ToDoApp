package com.richarddewan.paging3_todo.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class ViewModelProviderFactory<T:ViewModel>(private val kClass: KClass<T>, private val creator:()->T): ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(kClass.java)){
            return creator() as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}