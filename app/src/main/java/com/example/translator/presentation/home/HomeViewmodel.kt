package com.example.translator.presentation.home

import androidx.lifecycle.viewModelScope
import com.example.translator.common.UiState
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.domain.model.TranslatedWord
import com.example.translator.domain.usecase.DataStoreUseCase
import com.example.translator.domain.usecase.HomeUseCase
import com.example.translator.presentation.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel
    @Inject
    constructor(private val homeUseCase: HomeUseCase, private val dataStoreUseCase: DataStoreUseCase) :
    BaseViewmodel() {
        private val _listTranslatedWord = MutableStateFlow(TranslatedWord())
        val listTranslatedWord = _listTranslatedWord

        private val _pairLanguage: MutableStateFlow<Pair<SearchLanguageItem.LanguageItem?, SearchLanguageItem.LanguageItem?>> =
            MutableStateFlow(Pair(null, null))
        val pairLanguage = _pairLanguage

        var fromLanguageItem = SearchLanguageItem.LanguageItem()
        var toLanguageItem = SearchLanguageItem.LanguageItem()

        fun storeLanguageItem(
            isFromLanguageItem: Boolean,
            languageItem: SearchLanguageItem.LanguageItem,
        ) {
            viewModelScope.launch {
                dataStoreUseCase.storeLanguageItem(isFromLanguageItem, languageItem)
            }
        }

        fun swapLanguageItem(
            fromLanguageItem: SearchLanguageItem.LanguageItem,
            toLanguageItem: SearchLanguageItem.LanguageItem,
        ) {
            viewModelScope.launch {
                dataStoreUseCase.storeSwapLanguageItem(fromLanguageItem, toLanguageItem)
            }
        }

        // every time data store is updated, you can observe the data changes
        fun observePairLanguageItemChange() {
            dataStoreUseCase.pairLanguageItemFlow.onEach { result ->
                when (result) {
                    is UiState.Error -> TODO()
                    is UiState.Loading -> TODO()
                    is UiState.Success -> {
                        _pairLanguage.value =
                            Pair(result.data?.first, result.data?.second)
                    }
                }
            }.launchIn(viewModelScope)
        }

        fun addTranslatedWord(
            originalWord: String,
            translatedWord: String,
        ) {
            val translated = TranslatedWord(originalWord, translatedWord, false)

            homeUseCase.addTranslatedWord(translated).onEach {
                when (it) {
                    is UiState.Error -> {
                    }

                    is UiState.Loading -> {
                    }

                    is UiState.Success -> {
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
