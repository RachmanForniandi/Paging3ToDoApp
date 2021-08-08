package com.richarddewan.paging3_todo.ui.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.richarddewan.paging3_todo.util.MyApplication
import com.richarddewan.paging3_todo.R
import com.richarddewan.paging3_todo.adapter.TaskPagingDataAdapter
import com.richarddewan.paging3_todo.data.repository.flow.TaskFlowRepositoryImpl
import com.richarddewan.paging3_todo.data.repository.paging.TaskFlowPagingSource
import com.richarddewan.paging3_todo.databinding.FragmentFlowPagingSourceBinding
import com.richarddewan.paging3_todo.ui.flow.viewmodel.FlowViewModel
import com.richarddewan.paging3_todo.util.AppHelper
import com.richarddewan.paging3_todo.util.ViewModelProviderFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/*
created by Richard Dewan 11/04/2021
*/

class FlowPagingSourceFragment: Fragment() {
    private lateinit var binding: FragmentFlowPagingSourceBinding
    private lateinit var viewModel: FlowViewModel
    private lateinit var repository: TaskFlowRepositoryImpl
    private lateinit var pagingSource: TaskFlowPagingSource
    private lateinit var pagingDataAdapter: TaskPagingDataAdapter
    private val linearLayoutManager:LinearLayoutManager by lazy {
        LinearLayoutManager(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlowPagingSourceBinding.inflate(layoutInflater)

        //paging source
        pagingSource = TaskFlowPagingSource((requireActivity().application as MyApplication).networkService)

        //repository
        repository = TaskFlowRepositoryImpl(pagingSource)

        //viewmodel
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(FlowViewModel::class){
            FlowViewModel(repository)
        }).get(FlowViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagingDataAdapter = TaskPagingDataAdapter()

        binding.listItemFlow.apply {
            //layoutManager = linearLayoutManager
            adapter = pagingDataAdapter
        }

        pagingDataAdapter.addLoadStateListener { loadState->
            //show progress bar when the load state is loading
            binding.pgFlow.isVisible = loadState.source.refresh is LoadState.Loading

            //load state for error and show the msg on UI
            val errorState = loadState.source.refresh as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.append as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                AppHelper.showErrorSnackBar(binding.pgFlow,requireActivity(),it.error.message.toString())
            }

        }
        //observe the live data from viewmodel
        observers()
    }

    private fun observers() {
        lifecycleScope.launch {
            viewModel.getTaskListPaging()
                .collectLatest {
                    pagingDataAdapter.submitData(lifecycle,it)
                }
        }
    }

    /*private fun showErrorSnackBar(msg:String){
        Snackbar.make(requireView(),msg,Snackbar.LENGTH_INDEFINITE).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.purple_700))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setAction(R.string.close){
                dismiss()
            }
            anchorView = binding.pgFlow
        }.show()
    }*/
}