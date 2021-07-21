package com.richarddewan.paging3_todo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.richarddewan.paging3_todo.databinding.FragmentFlowPagingSourceBinding
import com.richarddewan.paging3_todo.databinding.FragmentFlowRemoteMediatorBinding


/*
created by Richard Dewan 11/04/2021
*/

class FlowRemoteMediatorFragment: Fragment() {
    private lateinit var binding: FragmentFlowRemoteMediatorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlowRemoteMediatorBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}