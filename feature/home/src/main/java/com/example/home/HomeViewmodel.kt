package com.example.home

import androidx.lifecycle.viewModelScope
import com.example.common.UiState
import com.example.domain.AddTranslationHistoryUseCase
import com.example.domain.DeleteTranslationHistoryUseCase
import com.example.domain.GetPairLanguageUseCase
import com.example.domain.GetTranslationHistoryUseCase
import com.example.domain.GetWordInformationUseCase
import com.example.domain.SpeakUseCase
import com.example.domain.StorePairLanguageUseCase
import com.example.domain.TranslateUseCase
import com.example.domain.UndoTranslationHistoryUseCase
import com.example.domain.UpdateFavoriteTranslationHistoryUseCase
import com.example.model.SearchLanguageItem
import com.example.model.TranslationHistory
import com.example.model.WordInformation
import com.example.ui.base.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        private val translateUseCase: TranslateUseCase,
        private val speakUseCase: SpeakUseCase,
    ) :
    BaseViewmodel() {
        // MutableStateFlow does not update the same address??
        private val _pairLanguageFlow =
            MutableStateFlow(Pair(SearchLanguageItem.LanguageItem(), SearchLanguageItem.LanguageItem()))
        val pairLanguageFlow: StateFlow<Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>> =
            _pairLanguageFlow.asStateFlow()

        private val _translatedTextFlow = MutableStateFlow("")
        val translatedTextFlow: StateFlow<String> = _translatedTextFlow.asStateFlow()

        private val _textDefinitionFlow = MutableStateFlow(WordInformation())
        val textDefinitionFlow: StateFlow<WordInformation> = _textDefinitionFlow.asStateFlow()

        private val _getTranslatedWordsFlow = MutableStateFlow(listOf<TranslationHistory>())
        val getTranslatedWordsFlow: StateFlow<List<TranslationHistory>> =
            _getTranslatedWordsFlow.asStateFlow()

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

        // every time data store is updated, you can observe the data changes
        fun observePairLanguageItemChange() {
            getPairLanguageUseCase.invoke(Unit).onEach { result ->
                when (result) {
                    is UiState.Error -> failureState.value = result.error
                    is UiState.Loading -> TODO()
                    is UiState.Success -> {
                        result.data.let { pairLanguage ->
                            _pairLanguageFlow.value = pairLanguage
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

        fun translate(
            originalText: String,
            fromLanguageCode: String,
            toLanguageCode: String,
        ) {
            viewModelScope.launch {
                val input =
                    TranslateUseCase.TranslationInput(
                        originalText,
                        fromLanguageCode,
                        toLanguageCode,
                    )
                when (val result = translateUseCase.invoke(input)) {
                    is UiState.Error -> failureState.value = result.error
                    is UiState.Loading -> TODO()
                    is UiState.Success -> {
                        result.data.let { translatedText ->
                            _translatedTextFlow.value = translatedText
                        }
                    }
                }
            }
        }

        // Find API to support multiple languages, support English only for now
        fun getWordDefinition(word: String) {
            if (_pairLanguageFlow.value.first.languageCode == "en") {
                viewModelScope.launch {
                    when (val uiState = getWordInformationUseCase.invoke(word)) {
                        is UiState.Error -> {
                            failureState.value = uiState.error
                            loadingState.value = false
                        }

                        is UiState.Loading -> {
                            loadingState.value = true
                        }

                        is UiState.Success -> {
                            uiState.data.let { wordInformation ->
                                _textDefinitionFlow.value = wordInformation
                            }
                            loadingState.value = false
                        }
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
            val input = SpeakUseCase.SpeakInput(text, languageCode)
            speakUseCase.invoke(input)
        }

        fun getTranslatedWords() {
            getTranslationHistoryUseCase.invoke(Unit).onEach { result ->
                when (result) {
                    is UiState.Error -> failureState.value = result.error
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
