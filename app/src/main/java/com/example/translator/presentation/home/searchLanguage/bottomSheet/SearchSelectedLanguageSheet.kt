package com.example.translator.presentation.home.searchLanguage.bottomSheet

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translator.R
import com.example.translator.databinding.SearchSelectedLanguageBinding
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.presentation.BaseBottomSheetFragment
import com.example.translator.presentation.home.searchLanguage.SearchLanguageViewModel
import com.example.translator.presentation.home.searchLanguage.adapter.SearchLanguageAdapter
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
    var clickItemButton: ((languageItem: SearchLanguageItem) -> Unit)? = null

    private var languageAdapter: SearchLanguageAdapter? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSearchLanguageItems()

        lifecycleScope.launch {
            launch {
                viewModel.listAllLanguages.collect { listLanguageItem ->
                    val listSearchLanguageItem = mutableListOf<SearchLanguageItem>()
                    listSearchLanguageItem.addTitle(getString(R.string.all_language))
                    listSearchLanguageItem.addAll(listLanguageItem)
                    initRecyclerView(listSearchLanguageItem)
                }
            }

            launch {
                viewModel.listFilterLanguages.collect { listSearchLanguageFilter ->
                    val listSearchLanguageItem = mutableListOf<SearchLanguageItem>()
                    listSearchLanguageItem.addTitle("")
                    listSearchLanguageItem.addAll(listSearchLanguageFilter)
                    updateLanguage(listSearchLanguageItem)
                }
            }
        }

        viewBinding.icClose.setOnClickListener {
            clickCloseButton?.invoke()
        }

        viewBinding.edtSearchLanguage.addTextChangedListener { text: Editable? ->
            val textFilter = text.toString()
            if (textFilter.isNotEmpty()) {
                viewModel.filterLanguage(textFilter)
            } else {
                val listSearchLanguageItem = mutableListOf<SearchLanguageItem>()
                listSearchLanguageItem.addTitle(getString(R.string.all_language))
                listSearchLanguageItem.addAll(viewModel.listAllLanguages.value)
                updateLanguage(listSearchLanguageItem)
            }
        }
    }

    private fun MutableList<SearchLanguageItem>.addTitle(title: String): List<SearchLanguageItem> {
        this.add(0, SearchLanguageItem.TitleItem(title))
        return this
    }

    private fun updateLanguage(listLanguageFilter: List<SearchLanguageItem>) {
        languageAdapter?.updateListLanguage(listLanguageFilter)
    }

    private fun initRecyclerView(listSearchLanguageItem: List<SearchLanguageItem>) {
        languageAdapter =
            SearchLanguageAdapter(listSearchLanguageItem) { searchLanguageItem ->
                clickItemButton?.invoke(searchLanguageItem)
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
