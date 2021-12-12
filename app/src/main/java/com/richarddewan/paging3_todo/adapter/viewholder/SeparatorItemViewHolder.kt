package com.richarddewan.paging3_todo.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.richarddewan.paging3_todo.R
import com.richarddewan.paging3_todo.databinding.SeparatorViewItemBinding

class SeparatorItemViewHolder (
    private val binding: SeparatorViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(status:String){
        binding.separatorDescription.text = status
    }
    companion object {
        fun create(parent: ViewGroup): SeparatorItemViewHolder {

            val view  = LayoutInflater.from(parent.context)
                .inflate(R.layout.separator_view_item,parent,false)

            val binding = SeparatorViewItemBinding.bind(view)

            return SeparatorItemViewHolder(binding)
        }
    }
}