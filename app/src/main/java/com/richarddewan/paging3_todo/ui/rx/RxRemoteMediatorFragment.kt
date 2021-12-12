package com.richarddewan.paging3_todo.ui.rx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.richarddewan.paging3_todo.adapter.TaskLoadStateAdapter
import com.richarddewan.paging3_todo.adapter.TaskRemoteMediatorDataAdapter
import com.richarddewan.paging3_todo.data.repository.paging.TaskFlowRemoteMediator
import com.richarddewan.paging3_todo.data.repository.paging.TaskRxRemoteMediator
import com.richarddewan.paging3_todo.data.repository.rx.TaskRxRemoteMediatorRepositoryImpl
import com.richarddewan.paging3_todo.databinding.FragmentFlowPagingSourceBinding
import com.richarddewan.paging3_todo.databinding.FragmentRxRemoteMediatorBinding
import com.richarddewan.paging3_todo.ui.flow.viewmodel.FlowRemoteMediatorViewModel
import com.richarddewan.paging3_todo.ui.rx.viewmodel.RxRemoteMediatorViewModel
import com.richarddewan.paging3_todo.util.AppHelper
import com.richarddewan.paging3_todo.util.MyApplication
import com.richarddewan.paging3_todo.util.ViewModelProviderFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable

class
RxRemoteMediatorFragment: Fragment() {
    private lateinit var binding: FragmentRxRemoteMediatorBinding
    private lateinit var viewModel: RxRemoteMediatorViewModel
    private lateinit var repository: TaskRxRemoteMediatorRepositoryImpl
    @ExperimentalPagingApi
    private lateinit var remoteMediator:TaskRxRemoteMediator
    private lateinit var remoteMediatorDataAdapter: TaskRemoteMediatorDataAdapter

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(requireActivity())
    }
    private val compositeDisposable = CompositeDisposable()
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
        //setup adapter
        remoteMediatorDataAdapter = TaskRemoteMediatorDataAdapter()
       //set recyclerview
        binding.listItemRxRemoteMediator.apply {
            //layoutManager = linearLayoutManager
            adapter = remoteMediatorDataAdapter
        }

        binding.listItemRxRemoteMediator.adapter =
            remoteMediatorDataAdapter.withLoadStateHeaderAndFooter(
                header = TaskLoadStateAdapter{
                    remoteMediatorDataAdapter.retry()},
                footer = TaskLoadStateAdapter{
                    remoteMediatorDataAdapter.retry()}
            )

        //load state
        remoteMediatorDataAdapter.addLoadStateListener { loadState ->
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
        observers()
    }

    @ExperimentalPagingApi
    private fun observers() {
        compositeDisposable.add(
            viewModel.getRxTaskList()
                .subscribe {
                    remoteMediatorDataAdapter.submitData(lifecycle,it)
                }
        )

    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}