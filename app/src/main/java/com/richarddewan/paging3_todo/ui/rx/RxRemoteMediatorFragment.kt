package com.richarddewan.paging3_todo.ui.rx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.richarddewan.paging3_todo.data.repository.paging.TaskFlowRemoteMediator
import com.richarddewan.paging3_todo.data.repository.paging.TaskRxRemoteMediator
import com.richarddewan.paging3_todo.data.repository.rx.TaskRxRemoteMediatorRepositoryImpl
import com.richarddewan.paging3_todo.databinding.FragmentFlowPagingSourceBinding
import com.richarddewan.paging3_todo.databinding.FragmentRxRemoteMediatorBinding
import com.richarddewan.paging3_todo.ui.flow.viewmodel.FlowRemoteMediatorViewModel
import com.richarddewan.paging3_todo.ui.rx.viewmodel.RxRemoteMediatorViewModel
import com.richarddewan.paging3_todo.util.MyApplication
import com.richarddewan.paging3_todo.util.ViewModelProviderFactory


/*
created by Richard Dewan 11/04/2021
*/

class
RxRemoteMediatorFragment: Fragment() {
    private lateinit var binding: FragmentRxRemoteMediatorBinding
    private lateinit var viewModel: RxRemoteMediatorViewModel
    private lateinit var repository: TaskRxRemoteMediatorRepositoryImpl
    @ExperimentalPagingApi
    private lateinit var remoteMediator:TaskRxRemoteMediator

    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRxRemoteMediatorBinding.inflate(layoutInflater)
        val netService = (requireActivity().application as MyApplication).networkService
        val dbService = (requireActivity().application as MyApplication).dbService

        remoteMediator = TaskRxRemoteMediator(netService,dbService)

        repository = TaskRxRemoteMediatorRepositoryImpl(dbService,remoteMediator)

        viewModel = ViewModelProvider(this, ViewModelProviderFactory(RxRemoteMediatorViewModel::class){
            RxRemoteMediatorViewModel(repository)
        }).get(RxRemoteMediatorViewModel::class.java)

        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()
    }

    @ExperimentalPagingApi
    private fun observers() {
        viewModel.getRxTaskList()
            .subscribe {

            }
    }
}