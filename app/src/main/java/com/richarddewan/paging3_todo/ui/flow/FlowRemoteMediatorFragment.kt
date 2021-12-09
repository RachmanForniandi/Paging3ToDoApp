package com.richarddewan.paging3_todo.ui.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.richarddewan.paging3_todo.data.repository.flow.TaskFlowRemoteMediatorRepositoryImpl
import com.richarddewan.paging3_todo.data.repository.paging.TaskFlowRemoteMediator
import com.richarddewan.paging3_todo.databinding.FragmentFlowPagingSourceBinding
import com.richarddewan.paging3_todo.databinding.FragmentFlowRemoteMediatorBinding
import com.richarddewan.paging3_todo.ui.flow.viewmodel.FlowRemoteMediatorViewModel
import com.richarddewan.paging3_todo.util.MyApplication
import com.richarddewan.paging3_todo.util.ViewModelProviderFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/*
created by Richard Dewan 11/04/2021
*/

class FlowRemoteMediatorFragment: Fragment() {
    private lateinit var binding: FragmentFlowRemoteMediatorBinding
    private lateinit var viewModel: FlowRemoteMediatorViewModel
    private lateinit var repository: TaskFlowRemoteMediatorRepositoryImpl

    @ExperimentalPagingApi
    private lateinit var remoteMediator:TaskFlowRemoteMediator

    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlowRemoteMediatorBinding.inflate(layoutInflater)

        val netService = (requireActivity().application as MyApplication).networkService
        val dbService = (requireActivity().application as MyApplication).dbService

        remoteMediator = TaskFlowRemoteMediator(netService,dbService)
        repository = TaskFlowRemoteMediatorRepositoryImpl(dbService,remoteMediator)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory(FlowRemoteMediatorViewModel::class){
            FlowRemoteMediatorViewModel(repository)
        }).get(FlowRemoteMediatorViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @ExperimentalPagingApi
    private fun observers(){
        lifecycleScope.launch {
            viewModel.getTaskList()
                .collectLatest {  }
        }
    }
}