package com.example.ui.bottomSheet.searchSelectedLanguage

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.model.Downloadable
import com.example.ui.R
import com.example.ui.base.BaseBottomSheetFragment
import com.example.ui.databinding.SearchSelectedLanguageBinding
import com.example.ui.util.AlertDialogUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchSelectedLanguageSheet : BaseBottomSheetFragment() {
    override val isCanceledOnTouchOutside: Boolean
        get() = false
    override val isFullScreen: Boolean
        get() = true
    override val viewBinding: SearchSelectedLanguageBinding by lazy {
        SearchSelectedLanguageBinding.inflate(
            layoutInflater,
        )
    }

    private val viewModel: SearchLanguageViewModel by viewModels()

    var clickCloseButton: (() -> Unit)? = null
    var clickItemButton: ((languageItem: com.example.model.SearchLanguageItem) -> Unit)? = null

    private var languageAdapter: SearchLanguageAdapter? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSearchLanguageItems()
        initRecyclerView(listOf())

        lifecycleScope.launch {
            launch {
                viewModel.listAllLanguages.collect { listLanguageItem ->
                    val listSearchLanguageItem = mutableListOf<com.example.model.SearchLanguageItem>()
                    listSearchLanguageItem.addTitle(getString(R.string.all_language))
                    listSearchLanguageItem.addAll(listLanguageItem)
                    updateLanguage(listSearchLanguageItem)
                }
            }

            launch {
                viewModel.listFilterLanguages.collect { listLanguageItemFilter ->
                    val listSearchLanguageItemFilter = mutableListOf<com.example.model.SearchLanguageItem>()
                    if (listLanguageItemFilter.size != viewModel.listAllLanguages.value.size) {
                        listSearchLanguageItemFilter.addTitle("")
                    } else {
                        listSearchLanguageItemFilter.addTitle(getString(R.string.all_language))
                    }
                    listSearchLanguageItemFilter.addAll(listLanguageItemFilter)
                    updateLanguage(listSearchLanguageItemFilter)
                }
            }

            launch {
                viewModel.downloadLanguageItem.collect { downloadedLanguageItem ->
                    viewModel.updateAllLanguages(downloadedLanguageItem)
                }
            }
        }

        viewBinding.icClose.setOnClickListener {
            clickCloseButton?.invoke()
        }

        viewBinding.edtSearchLanguage.addTextChangedListener { text: Editable? ->
            viewModel.filterLanguageItem(text.toString())
        }
    }

    private fun MutableList<com.example.model.SearchLanguageItem>.addTitle(title: String): List<com.example.model.SearchLanguageItem> {
        this.add(0, com.example.model.SearchLanguageItem.TitleItem(title))
        return this
    }

    private fun updateLanguage(listLanguageFilter: List<com.example.model.SearchLanguageItem>) {
        languageAdapter?.updateListLanguage(listLanguageFilter)
    }

    private fun initRecyclerView(listSearchLanguageItem: List<com.example.model.SearchLanguageItem>) {
        languageAdapter =
            SearchLanguageAdapter(listSearchLanguageItem) { languageItem ->
                if (languageItem.downloadable == Downloadable.NEED_DOWNLOAD) {
                    AlertDialogUtils.showAlertDialog(
                        requireContext(),
                        getString(R.string.download_language),
                        getString(R.string.download_language_message),
                        positiveText = getString(R.string.download),
                        onPositiveClick = {
                            viewModel.downloadLanguage(languageItem)
                        }, onNegativeClick = { dialog ->
                            dialog.dismiss()
                        },
                    )
                } else {
                    clickItemButton?.invoke(languageItem)
                }
            }

        viewBinding.rcvLanguage.apply {
            layoutManager =
                LinearLayoutManager(this@SearchSelectedLanguageSheet.requireContext())
            adapter = languageAdapter
        }
    }

    companion object {
        fun newInstance() = SearchSelectedLanguageSheet()

        val TAG: String = SearchSelectedLanguageSheet::class.java.simpleName
    }
}
