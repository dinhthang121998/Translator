package com.example.translator.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.translator.R
import com.example.translator.databinding.FragmentHomeBinding
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.presentation.BaseFragment
import com.example.translator.presentation.home.searchLanguage.bottomSheet.SearchSelectedLanguageSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        viewModel.observePairLanguageItemChange()
        binding.tvFromLanguage.setOnClickListener {
            showSearchBottomSheet { searchLanguageItem ->
                val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                binding.tvFromLanguage.text = languageItem.languageName
                viewModel.storeLanguageItem(true, languageItem)
            }
        }

        binding.tvToLanguage.setOnClickListener {
            showSearchBottomSheet { searchLanguageItem ->
                val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                binding.tvToLanguage.text = languageItem.languageName
                viewModel.storeLanguageItem(false, languageItem)
            }
        }

        binding.ivSwitch.setOnClickListener {
            viewModel.swapLanguageItem(viewModel.fromLanguageItem, viewModel.toLanguageItem)
        }

        lifecycleScope.launch {
            launch {
                viewModel.pairLanguage.collect { (fromLanguageItem, toLanguageItem) ->
                    fromLanguageItem?.let {
                        viewModel.fromLanguageItem = fromLanguageItem
                        binding.tvFromLanguage.text = fromLanguageItem.languageName
                    } ?: run { binding.tvFromLanguage.text = getString(R.string.search) }

                    toLanguageItem?.let {
                        viewModel.toLanguageItem = toLanguageItem
                        binding.tvToLanguage.text = toLanguageItem.languageName
                    } ?: run { binding.tvToLanguage.text = getString(R.string.search) }
                }
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
