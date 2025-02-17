package com.example.translator.presentation.home.searchLanguage

import androidx.lifecycle.viewModelScope
import com.example.translator.common.UiState
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.domain.usecase.LanguageUseCase
import com.example.translator.presentation.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchLanguageViewModel
    @Inject
    constructor(private val languageUseCase: LanguageUseCase) : BaseViewmodel() {
        private val _listAllLanguages =
            MutableStateFlow(mutableListOf<SearchLanguageItem.LanguageItem>())
        val listAllLanguages = _listAllLanguages

        private val _listFilterLanguages =
            MutableStateFlow(mutableListOf<SearchLanguageItem.LanguageItem>())
        val listFilterLanguages = _listFilterLanguages

        private val _downloadLanguageItem =
            MutableStateFlow(SearchLanguageItem.LanguageItem())
        val downloadLanguageItem = _downloadLanguageItem

        fun getSearchLanguageItems() {
            languageUseCase.getAllLanguageItems().onEach {
                when (it) {
                    is UiState.Error -> {}
                    is UiState.Loading -> {}

                    is UiState.Success -> {
                        it.data?.let { listLanguageItem ->
                            _listAllLanguages.value = listLanguageItem.toMutableList()
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

        fun filterLanguage(textFilter: String) {
            languageUseCase.filterLanguage(textFilter, _listAllLanguages.value)
                .onEach { filterList ->
                    _listFilterLanguages.value = filterList
                }.launchIn(viewModelScope)
        }

        fun updateAllLanguages(downloadedLanguageItem: SearchLanguageItem.LanguageItem) {
            languageUseCase.updateDownloadedLanguage(downloadedLanguageItem, _listAllLanguages.value)
                .onEach { listAfterUpdating ->
                    _listAllLanguages.value = listAfterUpdating
                }.launchIn(viewModelScope)
        }

        // TODO Implement loading when downloading language
        fun downloadLanguage(languageCode: SearchLanguageItem.LanguageItem) {
            languageUseCase.downloadLanguageModel(languageCode).onEach {
                when (it) {
                    is UiState.Error -> TODO()
                    is UiState.Loading -> TODO()
                    is UiState.Success -> {
                        it.data?.let { downloadLanguageItem ->
                            _downloadLanguageItem.value = downloadLanguageItem
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
