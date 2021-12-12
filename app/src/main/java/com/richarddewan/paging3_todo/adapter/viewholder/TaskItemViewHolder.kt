package com.richarddewan.paging3_todo.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.richarddewan.paging3_todo.R
import com.richarddewan.paging3_todo.data.local.entity.TaskEntity
import com.richarddewan.paging3_todo.databinding.TaskListViewBinding

class TaskItemViewHolder (
    private val binding: TaskListViewBinding
) : RecyclerView.ViewHolder(binding.root){


    fun onBind(data:TaskEntity){
        binding.lbTaskId.text = data.taskId.toString()
        binding.lbUserId.text = data.userId
        binding.lbTitle.text = data.title
        binding.lbBody.text = data.body
        binding.lbNote.text = data.note
        binding.lbStatus.text = data.status
    }
    companion object {
        fun create(parent: ViewGroup) : TaskItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_list_view,parent,false)

            val binding = TaskListViewBinding.bind(view)

            return TaskItemViewHolder(binding)
        }
    }
}