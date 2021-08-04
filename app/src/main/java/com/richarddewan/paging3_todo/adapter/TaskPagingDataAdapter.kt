package com.richarddewan.paging3_todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.richarddewan.paging3_todo.databinding.TaskListViewBinding
import com.richarddewan.paging3_todo.model.DataPaging

class TaskPagingDataAdapter(): PagingDataAdapter<DataPaging.Task, TaskPagingDataAdapter.TaskViewHolder>(
    diffCallback = diffUtil
) {

    companion object{
        val diffUtil = object :DiffUtil.ItemCallback<DataPaging.Task>(){
            override fun areItemsTheSame(
                oldItem: DataPaging.Task,
                newItem: DataPaging.Task
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DataPaging.Task,
                newItem: DataPaging.Task
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
    class TaskViewHolder(val binding:TaskListViewBinding):RecyclerView.ViewHolder(binding.root){

        fun onBind(data:DataPaging.Task){
            binding.lbTaskId.text = data.id
            binding.lbUserId.text = data.userId
            binding.lbTitle.text = data.title
            binding.lbBody.text = data.body
            binding.lbNote.text = data.note
            binding.lbStatus.text = data.status
        }
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int){
        getItem(position)?.let {
            holder.onBind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):TaskViewHolder {
        val binding = TaskListViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)
    }
}