package com.example.translator.presentation.home

import androidx.lifecycle.viewModelScope
import com.example.translator.common.UiState
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.domain.model.TranslatedWord
import com.example.translator.domain.model.WordInformation
import com.example.translator.domain.usecase.AddTranslatedWordUseCase
import com.example.translator.domain.usecase.GetPairLanguageUseCase
import com.example.translator.domain.usecase.GetWordInformationUseCase
import com.example.translator.domain.usecase.StorePairLanguageUseCase
import com.example.translator.presentation.BaseViewmodel
import com.example.translator.util.TextToSpeechUtils
import com.example.translator.util.TranslationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel
    @Inject
    constructor(
        private val getPairLanguageUseCase: GetPairLanguageUseCase,
        private val storePairLanguageUseCase: StorePairLanguageUseCase,
        private val addTranslatedWordUseCase: AddTranslatedWordUseCase,
        private val getWordInformationUseCase: GetWordInformationUseCase,
        private val textToSpeechUtils: TextToSpeechUtils,
        private val translationUtils: TranslationUtils,
    ) :
    BaseViewmodel() {
        // MutableStateFlow does not update the same address??
        private val _listTranslatedWord = MutableStateFlow(TranslatedWord())
        val listTranslatedWord = _listTranslatedWord

        private val _listAllLanguages =
            MutableStateFlow(mutableListOf<SearchLanguageItem.LanguageItem>())
        val listAllLanguages = _listAllLanguages

        private val _pairLanguage =
            MutableStateFlow(Pair(SearchLanguageItem.LanguageItem(), SearchLanguageItem.LanguageItem()))
        val pairLanguage = _pairLanguage

        private val _translatedTextState = MutableStateFlow("")
        val translatedTextState = _translatedTextState

        private val _swapTextState = MutableStateFlow(Pair("", ""))
        val swapTextState = _swapTextState

        private val _textDefinitionState = MutableStateFlow(WordInformation())
        val textDefinitionState = _textDefinitionState

        var fromLanguageItem = SearchLanguageItem.LanguageItem()
        var toLanguageItem = SearchLanguageItem.LanguageItem()

        var originalText = ""
        var translatedText = ""

        fun storeLanguageItem(
            isFromLanguageItem: Boolean,
            languageItem: SearchLanguageItem.LanguageItem,
        ) {
            viewModelScope.launch {
                val pairLanguage =
                    if (isFromLanguageItem) {
                        Pair(languageItem, _pairLanguage.value.second)
                    } else {
                        Pair(
                            _pairLanguage.value.first,
                            languageItem,
                        )
                    }
                storePairLanguageUseCase.invoke(pairLanguage)
            }
        }

        fun swapLanguageItem(
            fromLanguageItem: SearchLanguageItem.LanguageItem,
            toLanguageItem: SearchLanguageItem.LanguageItem,
        ) {
            viewModelScope.launch {
                val pairLanguage = Pair(toLanguageItem, fromLanguageItem)
                storePairLanguageUseCase.invoke(pairLanguage)
            }
        }

        fun swapText(
            fromText: String,
            toText: String,
        ) {
            _swapTextState.value = translationUtils.swapText(fromText, toText)
        }

        // every time data store is updated, you can observe the data changes
        fun observePairLanguageItemChange() {
            getPairLanguageUseCase.invoke(Unit).onEach { result ->
                when (result) {
                    is UiState.Error -> TODO()
                    is UiState.Loading -> TODO()
                    is UiState.Success -> {
                        result.data?.let { pairLanguage ->
                            _pairLanguage.value = pairLanguage
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

        // TODO: Handle error
        fun translate(
            originalText: String,
            fromLanguageCode: String,
            toLanguageCode: String,
        ) {
            viewModelScope.launch {
                _translatedTextState.value =
                    translationUtils.translate(originalText, fromLanguageCode, toLanguageCode)
            }
        }

        fun getWordDefinition(word: String) {
            getWordInformationUseCase.invoke(word).onEach { result ->
                when (result) {
                    is UiState.Error -> TODO()
                    is UiState.Loading -> {
                    }
                    is UiState.Success -> {
                        result.data?.let { wordInformation ->
                            _textDefinitionState.value = wordInformation
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

        fun addTranslatedWord(
            originalWord: String,
            translatedWord: String,
        ) {
            val translated = TranslatedWord(originalWord, translatedWord, false)
            addTranslatedWordUseCase.invoke(translated).launchIn(viewModelScope)
        }

        fun speak(
            text: String,
            languageCode: String,
        ) {
            textToSpeechUtils.speak(text, languageCode)
        }
    }
