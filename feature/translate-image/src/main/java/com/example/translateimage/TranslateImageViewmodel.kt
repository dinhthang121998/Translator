package com.example.translateimage

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.domain.TranslateTextFromImageUseCase
import com.example.common.UiState
import com.example.domain.GetPairLanguageUseCase
import com.example.domain.StorePairLanguageUseCase
import com.example.mlkit.ImageProcessor
import com.example.mlkit.TextRecognition
import com.example.mlkit.utils.TranslationUtils
import com.example.model.SearchLanguageItem
import com.example.model.TextDrawing
import com.example.ui.base.BaseViewmodel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateImageViewmodel
    @Inject
    constructor(
        private val storePairLanguageUseCase: StorePairLanguageUseCase,
        private val getPairLanguageUseCase: GetPairLanguageUseCase,
        private val translateTextFromImageUseCase: TranslateTextFromImageUseCase,
    ) :
    BaseViewmodel() {
        private val _listTextDrawing = MutableStateFlow<List<TextDrawing>>(listOf())
        val listTextDrawing: StateFlow<List<TextDrawing>> = _listTextDrawing.asStateFlow()

        private val _pairLanguageFlow =
            MutableStateFlow(Pair(SearchLanguageItem.LanguageItem(), SearchLanguageItem.LanguageItem()))
        val pairLanguageFlow: StateFlow<Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>> =
            _pairLanguageFlow.asStateFlow()

        var uri: Uri? = null

        // TODO add languageIdentify option
        fun processImage(
            inputImage: InputImage,
            option: TextRecognizerOptions,
        ) {
            viewModelScope.launch {
                val input = TranslateTextFromImageUseCase.TranslateTextFromImageInput(inputImage,
                    option,
                    _pairLanguageFlow.value.first,  _pairLanguageFlow.value.second)
                when (val result = translateTextFromImageUseCase.invoke(input)) {
                    is UiState.Error -> failureState.value = result.error
                    is UiState.Loading -> TODO()
                    is UiState.Success -> _listTextDrawing.value = result.data
                }
            }
        }

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
    }
