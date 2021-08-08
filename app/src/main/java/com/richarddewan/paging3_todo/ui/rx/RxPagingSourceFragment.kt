package com.richarddewan.paging3_todo.ui.rx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.richarddewan.paging3_todo.MyApplication
import com.richarddewan.paging3_todo.data.repository.flow.TaskFlowRepositoryImpl
import com.richarddewan.paging3_todo.data.repository.paging.TaskFlowPagingSource
import com.richarddewan.paging3_todo.data.repository.paging.TaskRxPagingSource
import com.richarddewan.paging3_todo.data.repository.rx.TaskRxRepositoryImpl
import com.richarddewan.paging3_todo.databinding.FragmentFlowPagingSourceBinding
import com.richarddewan.paging3_todo.databinding.FragmentRxPagingSourceBinding
import com.richarddewan.paging3_todo.ui.flow.viewmodel.FlowViewModel
import com.richarddewan.paging3_todo.ui.flow.viewmodel.ViewModelProviderFactory
import com.richarddewan.paging3_todo.ui.rx.viewmodel.RxViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/*
created by Richard Dewan 11/04/2021
*/

class RxPagingSourceFragment: Fragment() {
    private lateinit var binding: FragmentRxPagingSourceBinding
    private lateinit var viewModel: RxViewModel
    private lateinit var repositoryRx: TaskRxRepositoryImpl
    private lateinit var pagingSourceRx: TaskRxPagingSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRxPagingSourceBinding.inflate(layoutInflater)

        //paging source
        pagingSourceRx = TaskRxPagingSource((requireActivity().application as MyApplication).networkService)

        //repository
        repositoryRx = TaskRxRepositoryImpl(pagingSourceRx)

        //viewmodel
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(RxViewModel::class){
            RxViewModel(repositoryRx)
        }).get(RxViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observers() {
        viewModel.getRxTaskListPaging()
            .subscribe{

            }
    }
}