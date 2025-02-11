package com.example.translator.presentation.home.searchLanguage

import androidx.lifecycle.viewModelScope
import com.example.translator.common.UiState
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.domain.usecase.SearchLanguageUseCase
import com.example.translator.presentation.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchLanguageViewModel
    @Inject
    constructor(private val searchLanguageUseCase: SearchLanguageUseCase) : BaseViewmodel() {
        private val _listAllLanguages =
            MutableStateFlow(mutableListOf<SearchLanguageItem.LanguageItem>())
        val listAllLanguages = _listAllLanguages

        private val _listFilterLanguages =
            MutableStateFlow(mutableListOf<SearchLanguageItem.LanguageItem>())
        val listFilterLanguages = _listFilterLanguages

        fun getSearchLanguageItems() {
            searchLanguageUseCase.getSearchLanguageItems().onEach {
                when (it) {
                    is UiState.Error -> {}
                    is UiState.Loading -> {}

                    is UiState.Success -> {
                        it.data?.let { listLanguageItem ->
                            _listAllLanguages.value = listLanguageItem
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

        fun filterLanguage(textFilter: String) {
            searchLanguageUseCase.filterLanguage(textFilter, _listAllLanguages.value)
                .onEach { filterList ->
                    _listFilterLanguages.value = filterList
                }.flowOn(Dispatchers.IO)
        }
    }
