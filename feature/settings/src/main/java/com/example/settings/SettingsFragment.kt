package com.example.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.settings.databinding.FragmentSettingsBinding
import com.example.ui.base.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewmodel>() {

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: SettingsViewmodel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleClickItem()

    }

    private fun handleClickItem() {
        binding.feedback.onItemClick = {

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

        binding.clearTranslationHistory.onItemClick = {

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {

                }
            }

        const val TAG = "SettingsFragment"
    }
}