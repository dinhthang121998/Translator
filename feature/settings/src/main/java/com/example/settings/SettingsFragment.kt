package com.example.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.navigation.NavigateFeedback
import com.example.navigation.NavigateTheme
import com.example.settings.databinding.FragmentSettingsBinding
import com.example.ui.R
import com.example.ui.base.BaseFragment
import com.example.ui.util.AlertDialogUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewmodel>() {
    @Inject
    lateinit var navigateFeedback: NavigateFeedback

    @Inject
    lateinit var navigateTheme: NavigateTheme

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
            navigateFeedback.navigateToFeedback(requireContext())
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
            navigateTheme.navigateToTheme(requireContext())
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
