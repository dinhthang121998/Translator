package com.example.translator.presentation.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.translator.common.UiState
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.domain.model.TranslatedWord
import com.example.translator.domain.model.WordInformation
import com.example.translator.domain.usecase.DataStoreUseCase
import com.example.translator.domain.usecase.HomeUseCase
import com.example.translator.domain.usecase.LanguageUseCase
import com.example.translator.domain.usecase.SpeakingUseCase
import com.example.translator.domain.usecase.TranslationUseCase
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
constructor(
    private val homeUseCase: HomeUseCase,
    private val dataStoreUseCase: DataStoreUseCase,
    private val translationUseCase: TranslationUseCase,
    private val languageUseCase: LanguageUseCase,
    private val speakingUseCase: SpeakingUseCase
) :
    BaseViewmodel() {
    private val _listTranslatedWord = MutableStateFlow(TranslatedWord())
    val listTranslatedWord = _listTranslatedWord

    private val _listAllLanguages =
        MutableStateFlow(mutableListOf<SearchLanguageItem.LanguageItem>())
    val listAllLanguages = _listAllLanguages

    private val _pairLanguage: MutableStateFlow<Pair<SearchLanguageItem.LanguageItem?, SearchLanguageItem.LanguageItem?>> =
        MutableStateFlow(Pair(null, null))
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

    fun swapText(fromText: String, toText: String) {
        translationUseCase.swapText(fromText, toText).onEach { pairSwapText ->
            _swapTextState.value = pairSwapText
        }.launchIn(viewModelScope)
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

    fun translate(
        originalText: String,
        fromLanguageCode: String,
        toLanguageCode: String,
    ) {
        translationUseCase.translate(originalText, fromLanguageCode, toLanguageCode).onEach {
            when (it) {
                is UiState.Error -> TODO()
                is UiState.Loading -> TODO()
                is UiState.Success -> {
                    it.data?.let { translatedText ->
                        _translatedTextState.value = translatedText
                    }

                    Log.d("AAAA", "translatedText = ${it.data}")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getWordDefinition(word: String) {
        homeUseCase.getWordInformation(word).onEach { 
            when (it) {
                is UiState.Error -> {
                    Log.d("AAAA", "getWordDefinition error = ${it.message}")
                }
                is UiState.Loading -> {
                    Log.d("AAAA", "loading")
                }
                is UiState.Success -> {
                    it.data?.let { wordInformation ->
                        _textDefinitionState.value = wordInformation
                    }

                    Log.d("AAAA", "getWordDefinition success = ${it.data}")
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
                    Log.d("AAAA", "success")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun speak(text: String, languageCode: String) {
        speakingUseCase.speak(text, languageCode)
    }
}
