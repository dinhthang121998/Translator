package com.example.ui.bottomSheet.searchSelectedLanguage

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.mlkit.utils.TranslationUtils
import com.example.model.SearchLanguageItem
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
    // TODO: Remove the relationship between UI and ML kit
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
            val listFilter =
                translationUtils.filterLanguageItems(textFilter, _listAllLanguages.value)
            _listFilterLanguages.value = listFilter
        }
    }

    fun updateAllLanguages(downloadedLanguageItem: SearchLanguageItem.LanguageItem) {
        viewModelScope.launch {
            val updatedListLanguageItem = translationUtils.updateDownloadedLanguage(
                downloadedLanguageItem,
                _listAllLanguages.value
            )
            _listAllLanguages.value = updatedListLanguageItem
        }
    }


    // TODO Implement loading when downloading language. Handle error
    fun downloadLanguage(languageItem: SearchLanguageItem.LanguageItem) {
        viewModelScope.launch {
            loadingState.value = true
            _downloadLanguageItem.value = translationUtils.downloadLanguageModel(languageItem)
            loadingState.value = false
        }
    }
}
