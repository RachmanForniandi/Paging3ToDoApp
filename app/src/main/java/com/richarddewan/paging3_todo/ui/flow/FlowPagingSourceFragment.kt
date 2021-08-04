package com.richarddewan.paging3_todo.ui.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.richarddewan.paging3_todo.MyApplication
import com.richarddewan.paging3_todo.adapter.TaskPagingDataAdapter
import com.richarddewan.paging3_todo.data.repository.flow.TaskFlowRepositoryImpl
import com.richarddewan.paging3_todo.data.repository.paging.TaskFlowPagingSource
import com.richarddewan.paging3_todo.databinding.FragmentFlowPagingSourceBinding
import com.richarddewan.paging3_todo.ui.flow.viewmodel.FlowViewModel
import com.richarddewan.paging3_todo.ui.flow.viewmodel.ViewModelProviderFactory
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
        viewModel = ViewModelProvider(this,ViewModelProviderFactory(FlowViewModel::class){
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
}