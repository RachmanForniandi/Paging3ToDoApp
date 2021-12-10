package com.richarddewan.paging3_todo.ui.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.richarddewan.paging3_todo.adapter.TaskRemoteMediatorDataAdapter
import com.richarddewan.paging3_todo.data.repository.flow.TaskFlowRemoteMediatorRepositoryImpl
import com.richarddewan.paging3_todo.data.repository.paging.TaskFlowRemoteMediator
import com.richarddewan.paging3_todo.databinding.FragmentFlowPagingSourceBinding
import com.richarddewan.paging3_todo.databinding.FragmentFlowRemoteMediatorBinding
import com.richarddewan.paging3_todo.ui.flow.viewmodel.FlowRemoteMediatorViewModel
import com.richarddewan.paging3_todo.util.AppHelper
import com.richarddewan.paging3_todo.util.MyApplication
import com.richarddewan.paging3_todo.util.ViewModelProviderFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.wait


/*
created by Richard Dewan 11/04/2021
*/

class FlowRemoteMediatorFragment: Fragment() {
    private lateinit var binding: FragmentFlowRemoteMediatorBinding
    private lateinit var viewModel: FlowRemoteMediatorViewModel
    private lateinit var repository: TaskFlowRemoteMediatorRepositoryImpl


    @ExperimentalPagingApi
    private lateinit var remoteMediator:TaskFlowRemoteMediator

    private lateinit var remoteMediatorDataAdapter: TaskRemoteMediatorDataAdapter
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(requireActivity())
    }

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

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setup adapter
        remoteMediatorDataAdapter = TaskRemoteMediatorDataAdapter()

        //set recyclerview
        binding.listItemRemoteMediator.apply {
            //layoutManager = linearLayoutManager
            adapter = remoteMediatorDataAdapter
        }

        //load state
        remoteMediatorDataAdapter.addLoadStateListener {
            loadState->
            binding.pgRemoteMediator.isVisible = loadState.source.refresh is LoadState.Loading

            //load state for error and show the msg on UI
            val errorState = loadState.source.refresh as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.append as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                AppHelper.showErrorSnackBar(
                    binding.pgRemoteMediator,requireActivity(),
                    it.error.message.toString()
                )
            }
        }
        //observe the live data
        observers()

    }

    @ExperimentalPagingApi
    private fun observers(){
        lifecycleScope.launch {
            viewModel.getTaskList()
                .collectLatest {
                    remoteMediatorDataAdapter.submitData(lifecycle,it)
                }
        }
    }
}