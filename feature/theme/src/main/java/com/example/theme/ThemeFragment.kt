package com.example.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.theme.databinding.FragmentThemeBinding
import com.example.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ThemeFragment : BaseFragment<FragmentThemeBinding, ThemeViewmodel>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean,
    ): FragmentThemeBinding {
        return FragmentThemeBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: ThemeViewmodel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeTheme()
        lifecycleScope.launch {
            launch {
                viewModel.themeFlow.collect { isDarkMode ->
                    if (isDarkMode == null) return@collect

                    binding.darkTheme.updateSwitchState(isDarkMode)
                    val mode =
                        if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                    // TODO: Investigate why onCreate() of Fragment calls twice but once with onCreate() of Activity
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
        }
        binding.darkTheme.onClickSwitch = { isDarkMode ->
            viewModel.setTheme(isDarkMode)
        }

        binding.customToolbar.onBackAreaClick = {
            requireActivity().finish()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ThemeFragment().apply {
            }

        const val TAG = "ThemeFragment"
    }
}
