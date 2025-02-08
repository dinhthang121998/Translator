package com.example.translator.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.translator.R
import com.example.translator.databinding.FragmentHomeBinding

abstract class BaseFragment<V: ViewBinding, M: ViewModel>: Fragment() {

    private var _binding: V? = null
    val binding get() = _binding!!

    abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean): V?

    abstract val viewModel: M

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initBinding(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}