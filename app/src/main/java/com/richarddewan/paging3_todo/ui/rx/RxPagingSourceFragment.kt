package com.richarddewan.paging3_todo.ui.rx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.richarddewan.paging3_todo.util.MyApplication
import com.richarddewan.paging3_todo.R
import com.richarddewan.paging3_todo.adapter.TaskPagingDataAdapter
import com.richarddewan.paging3_todo.data.repository.paging.TaskRxPagingSource
import com.richarddewan.paging3_todo.data.repository.rx.TaskRxRepositoryImpl
import com.richarddewan.paging3_todo.databinding.FragmentRxPagingSourceBinding
import com.richarddewan.paging3_todo.util.ViewModelProviderFactory
import com.richarddewan.paging3_todo.ui.rx.viewmodel.RxViewModel
import com.richarddewan.paging3_todo.util.AppHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi


/*
created by Richard Dewan 11/04/2021
*/

class RxPagingSourceFragment: Fragment() {
    private lateinit var binding: FragmentRxPagingSourceBinding
    private lateinit var viewModel: RxViewModel
    private lateinit var repositoryRx: TaskRxRepositoryImpl
    private lateinit var pagingSourceRx: TaskRxPagingSource
    private lateinit var pagingDataAdapter: TaskPagingDataAdapter
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(requireActivity())
    }

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

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagingDataAdapter = TaskPagingDataAdapter()

        binding.listItemRx.apply {
            adapter = pagingDataAdapter
        }

        pagingDataAdapter.addLoadStateListener { loadState->
            //show progress bar when the load state is loading
            binding.pgRx.isVisible = loadState.source.refresh is LoadState.Loading

            //load state for error and show the msg on UI
            val errorState = loadState.source.refresh as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.append as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                AppHelper.showErrorSnackBar(
                    binding.pgRx,requireActivity(),it.error.message.toString()
                )
            }
        }
        //observe livedata from viewmodel
        observers()
    }



    @ExperimentalCoroutinesApi
    private fun observers() {
        viewModel.getRxTaskListPaging()
            .subscribe{
                pagingDataAdapter.submitData(lifecycle,it)
            }
    }

    /*private fun showErrorSnackBarRx(msg: String) {
        Snackbar.make(requireView(),msg, Snackbar.LENGTH_INDEFINITE).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.purple_700))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setAction(R.string.close){
                dismiss()
            }
            anchorView = binding.pgRx
        }.show()
    }*/
}