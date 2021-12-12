package com.richarddewan.paging3_todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.richarddewan.paging3_todo.R
import com.richarddewan.paging3_todo.databinding.LoadStateFooterViewItemBinding

class LoadStateViewHolder (private val binding:LoadStateFooterViewItemBinding,
private val retry:()-> Unit):RecyclerView.ViewHolder(binding.root){

    init {
        binding.retryButton.setOnClickListener {
            retry.invoke()
        }
    }

    fun onBind(loadState: LoadState){
        when(loadState){
            is LoadState.Loading->{
                binding.retryButton.isVisible = false
                binding.errorMsg.isVisible = false
                binding.progressBar.isVisible = true
            }
            is LoadState.NotLoading->{
                binding.retryButton.isVisible = false
                binding.errorMsg.isVisible = false
                binding.progressBar.isVisible = false
            }
            is LoadState.Error->{
                binding.errorMsg.text= loadState.error.message
                binding.retryButton.isVisible = false
                binding.errorMsg.isVisible = false
                binding.progressBar.isVisible = true
            }
        }
    }

    companion object{
        fun create(parent:ViewGroup,retry: () -> Unit):LoadStateViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.load_state_footer_view_item,parent,false)
            val  binding = LoadStateFooterViewItemBinding.bind(view)
            return LoadStateViewHolder(binding,retry)
        }
    }
}