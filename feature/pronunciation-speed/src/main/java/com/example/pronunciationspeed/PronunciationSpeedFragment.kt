package com.example.pronunciationspeed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pronunciationspeed.databinding.FragmentPronunciationSpeedBinding
import com.example.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PronunciationSpeedFragment :
    BaseFragment<FragmentPronunciationSpeedBinding, PronunciationSpeedViewmodel>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean,
    ): FragmentPronunciationSpeedBinding {
        return FragmentPronunciationSpeedBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: PronunciationSpeedViewmodel by viewModels()

    private var pronunciationSpeedAdapter: PronunciationSpeedAdapter? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPronunciationSpeed()

        lifecycleScope.launch {
            launch {
                viewModel.pronunciationSpeed.collect { listPronunciationSpeed ->
                    Log.d("AAAA", "listPronunciationSpeed: $listPronunciationSpeed")
                    pronunciationSpeedAdapter?.updatedListPronunciationSpeed(listPronunciationSpeed)
                }
            }
        }

        initPronunciationSpeedAdapter()
        binding.toolbar.onBackAreaClick = {
            requireActivity().finish()
        }
    }

    private fun initPronunciationSpeedAdapter() {
        pronunciationSpeedAdapter =
            PronunciationSpeedAdapter(listOf(), onClickItem = { pronunciationSpeed ->
                viewModel.savePronunciationSpeed(pronunciationSpeed)
            }, onSoundClick = { pronunciationSpeed ->
            })
        binding.rcvPronunciationSpeed.apply {
            adapter = pronunciationSpeedAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PronunciationSpeedFragment().apply {
                arguments =
                    Bundle().apply {
                    }
            }

        const val TAG = "PronunciationSpeedFragment"
    }
}
