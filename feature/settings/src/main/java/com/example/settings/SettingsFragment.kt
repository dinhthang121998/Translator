package com.example.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.feedback.FeedbackActivity
import com.example.settings.databinding.FragmentSettingsBinding
import com.example.theme.ThemeActivity
import com.example.ui.R
import com.example.ui.base.BaseFragment
import com.example.ui.util.AlertDialogUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewmodel>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean,
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: SettingsViewmodel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        handleClickItem()
    }

    private fun handleClickItem() {
        binding.feedback.onItemClick = {
            val intent = Intent(requireActivity(), FeedbackActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.about.onItemClick = {
        }

        binding.help.onItemClick = {
        }

        binding.speechInput.onItemClick = {
        }

        binding.pronunciationSpeed.onItemClick = {
        }

        binding.keyboard.onItemClick = {
        }

        binding.sendCrash.onItemClick = {
        }

        binding.darkTheme.onItemClick = {
            val intent = Intent(requireActivity(), ThemeActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.clearTranslationHistory.onItemClick = {
            showDeleteTranslationHistoryConfirmationDialog()
        }
    }

    private fun showDeleteTranslationHistoryConfirmationDialog() {
        AlertDialogUtils.showAlertDialog(
            requireContext(),
            getString(R.string.clear_translation_history),
            getString(R.string.clear_translation_history_message),
            onPositiveClick = {
                viewModel.clearAllTranslationHistory()
            },
            onNegativeClick = { dialogInterface ->
                dialogInterface.dismiss()
            },
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments =
                    Bundle().apply {
                    }
            }

        const val TAG = "SettingsFragment"
    }
}
