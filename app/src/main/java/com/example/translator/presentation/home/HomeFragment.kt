package com.example.translator.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.translator.databinding.FragmentHomeBinding
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.presentation.BaseFragment
import com.example.translator.presentation.home.searchLanguage.bottomSheet.SearchSelectedLanguageSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewmodel>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean,
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: HomeViewmodel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFromLanguage.setOnClickListener {
            showSearchBottomSheet { searchLanguageItem ->
                val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                binding.tvFromLanguage.text = languageItem.languageName
            }
        }

        binding.tvToLanguage.setOnClickListener {
            showSearchBottomSheet { searchLanguageItem ->
                val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                binding.tvToLanguage.text = languageItem.languageName
            }
        }
    }

    private fun showSearchBottomSheet(clickItem: ((SearchLanguageItem) -> Unit)? = null) {
        val bottomSheetLanguage =
            SearchSelectedLanguageSheet.newInstance().apply {
                clickCloseButton = {
                    this.dismiss()
                }
                clickItemButton = { languageItem: SearchLanguageItem ->
                    clickItem?.invoke(languageItem)
                    this.dismiss()
                }
            }
        bottomSheetLanguage.show(childFragmentManager, SearchSelectedLanguageSheet.TAG)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
