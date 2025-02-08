package com.example.translator.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.translator.R
import com.example.translator.databinding.FragmentHomeBinding
import com.example.translator.domain.model.DownloadLanguage
import com.example.translator.domain.model.Downloadable
import com.example.translator.presentation.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewmodel>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: HomeViewmodel by viewModels()

    private val items = mutableListOf<DownloadLanguage>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items.add(DownloadLanguage("English", Downloadable.NO_NEED_DOWNLOAD))
        items.add(DownloadLanguage("Vietnamese", Downloadable.IS_DOWNLOADED))
        items.add(DownloadLanguage("Chinese", Downloadable.NEED_DOWNLOAD))

    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}