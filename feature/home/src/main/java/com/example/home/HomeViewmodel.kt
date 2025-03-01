package com.example.home

import androidx.lifecycle.viewModelScope
import com.example.common.UiState
import com.example.domain.AddTranslatedWordUseCase
import com.example.domain.GetPairLanguageUseCase
import com.example.domain.GetTranslatedWordUseCase
import com.example.domain.GetWordInformationUseCase
import com.example.domain.StorePairLanguageUseCase
import com.example.domain.UpdateTranslatedFavoriteUseCase
import com.example.mlkit.utils.TranslationUtils
import com.example.ui.base.BaseViewmodel
import com.example.voice.TextToSpeechUtils
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
        private val getTranslatedWordUseCase: GetTranslatedWordUseCase,
        private val updateTranslatedFavoriteUseCase: UpdateTranslatedFavoriteUseCase,
        private val textToSpeechUtils: TextToSpeechUtils,
        private val translationUtils: TranslationUtils,
    ) :
    BaseViewmodel() {
        // MutableStateFlow does not update the same address??
        private val _pairLanguageFlow =
            MutableStateFlow(Pair(com.example.model.SearchLanguageItem.LanguageItem(), com.example.model.SearchLanguageItem.LanguageItem()))
        val pairLanguageFlow = _pairLanguageFlow

        private val _translatedTextFlow = MutableStateFlow("")
        val translatedTextFlow = _translatedTextFlow

        private val _swapTextFlow = MutableStateFlow(Pair("", ""))
        val swapTextFlow = _swapTextFlow

        private val _textDefinitionFlow = MutableStateFlow(com.example.model.WordInformation())
        val textDefinitionFlow = _textDefinitionFlow

        private val _getTranslatedWordsFlow = MutableStateFlow(listOf<com.example.model.TranslatedWord>())
        val getTranslatedWordsFlow = _getTranslatedWordsFlow

        var fromLanguageItem = com.example.model.SearchLanguageItem.LanguageItem()
        var toLanguageItem = com.example.model.SearchLanguageItem.LanguageItem()

        var originalText = ""
        var translatedText = ""

        fun storeLanguageItem(
            isFromLanguageItem: Boolean,
            languageItem: com.example.model.SearchLanguageItem.LanguageItem,
        ) {
            val pairLanguage =
                if (isFromLanguageItem) {
                    Pair(languageItem, _pairLanguageFlow.value.second)
                } else {
                    Pair(
                        _pairLanguageFlow.value.first,
                        languageItem,
                    )
                }

            // Use launchIn(viewModelScope) instead of viewModelScope.launch { your code } to complete the flow
            // or viewModelScope.launch { useCaseFlow.invoke().collect { empty here for Unit }}
            storePairLanguageUseCase.invoke(pairLanguage)
                .launchIn(viewModelScope)
        }

        fun swapLanguageItem(
            fromLanguageItem: com.example.model.SearchLanguageItem.LanguageItem,
            toLanguageItem: com.example.model.SearchLanguageItem.LanguageItem,
        ) {
            val pairLanguage = Pair(toLanguageItem, fromLanguageItem)
            storePairLanguageUseCase.invoke(pairLanguage)
                .launchIn(viewModelScope)
        }

        fun swapText(
            fromText: String,
            toText: String,
        ) {
            _swapTextFlow.value = translationUtils.swapText(fromText, toText)
        }

        // every time data store is updated, you can observe the data changes
        fun observePairLanguageItemChange() {
            getPairLanguageUseCase.invoke(Unit).onEach { result ->
                when (result) {
                    is UiState.Error -> TODO()
                    is UiState.Loading -> TODO()
                    is UiState.Success -> {
                        result.data?.let { pairLanguage ->
                            _pairLanguageFlow.value = pairLanguage
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
                _translatedTextFlow.value =
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
                            _textDefinitionFlow.value = wordInformation
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

        fun addTranslatedWord(
            originalWord: String,
            translatedWord: String,
        ) {
            val translated =
                com.example.model.TranslatedWord(
                    originalWord = originalWord,
                    translatedWord = translatedWord,
                    isFavourite = false,
                )
            addTranslatedWordUseCase.invoke(translated)
                .launchIn(viewModelScope)
        }

        fun speak(
            text: String,
            languageCode: String,
        ) {
            textToSpeechUtils.speak(text, languageCode)
        }

        fun getTranslatedWords() {
            getTranslatedWordUseCase.invoke(Unit).onEach { result ->
                when (result) {
                    is UiState.Error -> TODO()
                    is UiState.Loading -> TODO()
                    is UiState.Success -> {
                        result.data?.let { translatedWords ->
                            _getTranslatedWordsFlow.value = translatedWords
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

        fun updateTranslatedFavorite(translatedWord: com.example.model.TranslatedWord) {
            val updatedTranslatedWord = translatedWord.copy(isFavourite = !translatedWord.isFavourite)
            updateTranslatedFavoriteUseCase.invoke(updatedTranslatedWord).launchIn(viewModelScope)
        }
    }
