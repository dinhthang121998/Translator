package com.example.ui.bottomSheet.searchSelectedLanguage

import androidx.lifecycle.viewModelScope
import com.example.mlkit.utils.TranslationUtils
import com.example.ui.base.BaseViewmodel
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
            MutableStateFlow(listOf<com.example.model.SearchLanguageItem.LanguageItem>())
        val listAllLanguages = _listAllLanguages

        private val _listFilterLanguages =
            MutableStateFlow(listOf<com.example.model.SearchLanguageItem.LanguageItem>())
        val listFilterLanguages = _listFilterLanguages

        private val _downloadLanguageItem =
            MutableStateFlow(com.example.model.SearchLanguageItem.LanguageItem())
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

        fun updateAllLanguages(downloadedLanguageItem: com.example.model.SearchLanguageItem.LanguageItem) {
            viewModelScope.launch {
                val updatedListLanguageItem = translationUtils.updateDownloadedLanguage(downloadedLanguageItem, _listAllLanguages.value)
                _listAllLanguages.value = updatedListLanguageItem
            }
        }

        // TODO Implement loading when downloading language. Handle error
        fun downloadLanguage(languageItem: com.example.model.SearchLanguageItem.LanguageItem) {
            viewModelScope.launch {
                _downloadLanguageItem.value = translationUtils.downloadLanguageModel(languageItem)
            }
        }
    }
