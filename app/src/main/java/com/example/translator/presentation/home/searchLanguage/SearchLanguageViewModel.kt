package com.example.translator.presentation.home.searchLanguage

import androidx.lifecycle.viewModelScope
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.presentation.BaseViewmodel
import com.example.translator.util.TranslationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchLanguageViewModel
    @Inject
    constructor(
        private val translationUtils: TranslationUtils,
    ) : BaseViewmodel() {
        private val _listAllLanguages =
            MutableStateFlow(listOf<SearchLanguageItem.LanguageItem>())
        val listAllLanguages = _listAllLanguages

        private val _listFilterLanguages =
            MutableStateFlow(listOf<SearchLanguageItem.LanguageItem>())
        val listFilterLanguages = _listFilterLanguages

        private val _downloadLanguageItem =
            MutableStateFlow(SearchLanguageItem.LanguageItem())
        val downloadLanguageItem = _downloadLanguageItem

        // TODO Handle error
        fun getSearchLanguageItems() {
            viewModelScope.launch {
                _listAllLanguages.value = translationUtils.getAllLanguageItems()
            }
        }

        fun filterLanguageItem(textFilter: String) {
            viewModelScope.launch {
                val listFilter = translationUtils.filterLanguageItems(textFilter, _listAllLanguages.value)
                _listFilterLanguages.value = listFilter
            }
        }

        fun updateAllLanguages(downloadedLanguageItem: SearchLanguageItem.LanguageItem) {
            viewModelScope.launch {
                val updatedListLanguageItem = translationUtils.updateDownloadedLanguage(downloadedLanguageItem, _listAllLanguages.value)
                _listAllLanguages.value = updatedListLanguageItem
            }
        }

        // TODO Implement loading when downloading language. Handle error
        fun downloadLanguage(languageItem: SearchLanguageItem.LanguageItem) {
            viewModelScope.launch {
                _downloadLanguageItem.value = translationUtils.downloadLanguageModel(languageItem)
            }
        }
    }
