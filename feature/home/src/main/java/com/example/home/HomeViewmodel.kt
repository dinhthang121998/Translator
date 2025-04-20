package com.example.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.common.UiState
import com.example.domain.AddTranslatedWordUseCase
import com.example.domain.DeleteTranslatedWordUseCase
import com.example.domain.GetPairLanguageUseCase
import com.example.domain.GetTranslatedWordUseCase
import com.example.domain.GetWordInformationUseCase
import com.example.domain.StorePairLanguageUseCase
import com.example.domain.UpdateTranslatedFavoriteUseCase
import com.example.mlkit.utils.TranslationUtils
import com.example.model.SearchLanguageItem
import com.example.model.TranslatedWord
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
        private val addTranslatedWordUseCase: AddTranslatedWordUseCase,
        private val deleteTranslatedWordUseCase: DeleteTranslatedWordUseCase,
        private val getWordInformationUseCase: GetWordInformationUseCase,
        private val getTranslatedWordUseCase: GetTranslatedWordUseCase,
        private val updateTranslatedFavoriteUseCase: UpdateTranslatedFavoriteUseCase,
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

        private val _getTranslatedWordsFlow = MutableStateFlow(listOf<TranslatedWord>())
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

        fun addTranslatedWord(
            originalWord: String,
            translatedWord: String,
        ) {
            viewModelScope.launch {
                val existedTranslatedWord =
                    _getTranslatedWordsFlow.value.find {
                        it.originalWord == originalWord && it.translatedWord == translatedWord
                    }

                Log.d("AAAA", "existedTranslatedWord = $existedTranslatedWord")

                val translated =
                    TranslatedWord(
                        id = existedTranslatedWord?.id ?: 0,
                        originalWord = originalWord,
                        translatedWord = translatedWord,
                        isFavourite = existedTranslatedWord?.isFavourite ?: false,
                        createdAt = existedTranslatedWord?.createdAt ?: System.currentTimeMillis(),
                    )

                Log.d("AAAA", "translated = $translated")
                addTranslatedWordUseCase.invoke(translated)
            }
        }

        fun deleteTranslatedWord(translatedWord: TranslatedWord) {
            viewModelScope.launch {
                deleteTranslatedWordUseCase.invoke(translatedWord)
            }
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
                        result.data.let { translatedWords ->
                            _getTranslatedWordsFlow.value = translatedWords
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

        fun updateTranslatedFavorite(translatedWord: TranslatedWord) {
            viewModelScope.launch {
                val updatedTranslatedWord = translatedWord.copy(isFavourite = !translatedWord.isFavourite)
                updateTranslatedFavoriteUseCase.invoke(updatedTranslatedWord)
            }
        }
    }
