package com.example.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.common.UiState
import com.example.domain.GetWordInformationUseCase
import com.example.domain.pairLanguage.GetPairLanguageUseCase
import com.example.domain.pairLanguage.StorePairLanguageUseCase
import com.example.domain.translationHistory.AddTranslationHistoryUseCase
import com.example.domain.translationHistory.DeleteTranslationHistoryUseCase
import com.example.domain.translationHistory.GetTranslationHistoryUseCase
import com.example.domain.translationHistory.UndoTranslationHistoryUseCase
import com.example.domain.translationHistory.UpdateFavoriteTranslationHistoryUseCase
import com.example.mlkit.utils.TranslationUtils
import com.example.model.SearchLanguageItem
import com.example.model.TranslationHistory
import com.example.model.WordInformation
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
        private val addTranslationHistoryUseCase: AddTranslationHistoryUseCase,
        private val deleteTranslationHistoryUseCase: DeleteTranslationHistoryUseCase,
        private val getWordInformationUseCase: GetWordInformationUseCase,
        private val getTranslationHistoryUseCase: GetTranslationHistoryUseCase,
        private val updateFavoriteTranslationHistoryUseCase: UpdateFavoriteTranslationHistoryUseCase,
        private val undoTranslationHistoryUseCase: UndoTranslationHistoryUseCase,
        private val textToSpeechUtils: TextToSpeechUtils,
        private val translationUtils: TranslationUtils,
    ) :
    BaseViewmodel() {
        // MutableStateFlow does not update the same address??
        private val _pairLanguageFlow =
            MutableStateFlow(Pair(SearchLanguageItem.LanguageItem(), SearchLanguageItem.LanguageItem()))
        val pairLanguageFlow = _pairLanguageFlow

        private val _translatedTextFlow = MutableStateFlow("")
        val translatedTextFlow = _translatedTextFlow

//        private val _swapTextFlow = MutableStateFlow(Pair("", ""))
//        val swapTextFlow = _swapTextFlow

        private val _textDefinitionFlow = MutableStateFlow(WordInformation())
        val textDefinitionFlow = _textDefinitionFlow

        private val _getTranslatedWordsFlow = MutableStateFlow(listOf<TranslationHistory>())
        val getTranslatedWordsFlow = _getTranslatedWordsFlow

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
                        Pair(languageItem, _pairLanguageFlow.value.second)
                    } else {
                        Pair(
                            _pairLanguageFlow.value.first,
                            languageItem,
                        )
                    }
                storePairLanguageUseCase.invoke(pairLanguage)
            }
        }

//        fun swapLanguageItem(
//            fromLanguageItem: SearchLanguageItem.LanguageItem,
//            toLanguageItem: SearchLanguageItem.LanguageItem,
//        ) {
//            val pairLanguage = Pair(toLanguageItem, fromLanguageItem)
//            storePairLanguageUseCase.invoke(pairLanguage)
//                .launchIn(viewModelScope)
//        }
//
//        fun swapText(
//            fromText: String,
//            toText: String,
//        ) {
//            _swapTextFlow.value = translationUtils.swapText(fromText, toText)
//        }

        // every time data store is updated, you can observe the data changes
        fun observePairLanguageItemChange() {
            getPairLanguageUseCase.invoke(Unit).onEach { result ->
                when (result) {
                    is UiState.Error -> TODO()
                    is UiState.Loading -> TODO()
                    is UiState.Success -> {
                        result.data.let { pairLanguage ->
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
            viewModelScope.launch {
                when (val uiState = getWordInformationUseCase.invoke(word)) {
                    is UiState.Error -> {
                        Log.d("AAAA", "error = ${uiState.error}")
                        loadingFlow.value = false
                    }

                    is UiState.Loading -> {
                        loadingFlow.value = true
                    }

                    is UiState.Success -> {
                        uiState.data.let { wordInformation ->
                            _textDefinitionFlow.value = wordInformation
                        }
                        loadingFlow.value = false
                    }
                }
            }
        }

        fun addTranslationHistory(
            originalWord: String,
            translatedWord: String,
        ) {
            viewModelScope.launch {
                val translated =
                    TranslationHistory(
                        originalWord = originalWord,
                        translatedWord = translatedWord,
                        createdAt = System.currentTimeMillis(),
                    )

                Log.d("AAAA", "translated = $translated")
                addTranslationHistoryUseCase.invoke(translated)
            }
        }

        fun deleteTranslationHistory(id: Int) {
            viewModelScope.launch {
                deleteTranslationHistoryUseCase.invoke(id)
            }
        }

        fun speak(
            text: String,
            languageCode: String,
        ) {
            textToSpeechUtils.speak(text, languageCode)
        }

        fun getTranslatedWords() {
            getTranslationHistoryUseCase.invoke(Unit).onEach { result ->
                when (result) {
                    is UiState.Error -> TODO()
                    is UiState.Loading -> TODO()
                    is UiState.Success -> {
                        result.data.let { translatedWords ->
                            _getTranslatedWordsFlow.value = translatedWords
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

        fun updateTranslatedFavorite(translatedWord: TranslationHistory) {
            viewModelScope.launch {
                updateFavoriteTranslationHistoryUseCase.invoke(translatedWord)
            }
        }

        fun undoTranslationHistory(translationHistory: TranslationHistory) {
            viewModelScope.launch {
                undoTranslationHistoryUseCase.invoke(translationHistory)
            }
        }
    }
