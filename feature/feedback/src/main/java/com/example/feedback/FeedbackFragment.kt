package com.example.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.feedback.databinding.FragmentFeedbackBinding
import com.example.ui.base.BaseFragment

class FeedbackFragment : BaseFragment<FragmentFeedbackBinding, FeedbackViewmodel>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean,
    ): FragmentFeedbackBinding {
        return FragmentFeedbackBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: FeedbackViewmodel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.customToolbar.onBackAreaClick = {
            requireActivity().finish()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FeedbackFragment().apply {
            }

        const val TAG = "FeedbackFragment"
    }
}
