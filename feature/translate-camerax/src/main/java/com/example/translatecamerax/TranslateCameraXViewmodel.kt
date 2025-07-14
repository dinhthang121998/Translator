package com.example.translatecamerax

import androidx.camera.core.ImageProxy
import androidx.lifecycle.viewModelScope
import com.example.common.UiState
import com.example.domain.GetPairLanguageUseCase
import com.example.domain.StorePairLanguageUseCase
import com.example.domain.TranslateTextFromCameraUseCase
import com.example.model.SearchLanguageItem
import com.example.model.TextDrawing
import com.example.ui.base.BaseViewmodel
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateCameraXViewmodel
    @Inject
    constructor(
        private val translateTextFromCameraUseCase: TranslateTextFromCameraUseCase,
        private val storePairLanguageUseCase: StorePairLanguageUseCase,
        private val getPairLanguageUseCase: GetPairLanguageUseCase,
    ) :
    BaseViewmodel() {
        private val _textStateFlow = MutableStateFlow<List<TextDrawing>>(listOf())
        val textStateFlow: StateFlow<List<TextDrawing>> = _textStateFlow.asStateFlow()

        private val _pairLanguageFlow =
            MutableStateFlow(Pair(SearchLanguageItem.LanguageItem(), SearchLanguageItem.LanguageItem()))
        val pairLanguageFlow: StateFlow<Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>> =
            _pairLanguageFlow.asStateFlow()

        // TODO: Remove language hardcode
        fun processImageProxy(
            imageProxy: ImageProxy,
            option: TextRecognizerOptions,
        ) {
            viewModelScope.launch {
                val input =
                    TranslateTextFromCameraUseCase.TranslateTextFromCameraInput(
                        imageProxy,
                        option,
                        _pairLanguageFlow.value.first,
                        _pairLanguageFlow.value.second,
                    )
                when (val result = translateTextFromCameraUseCase.invoke(input)) {
                    is UiState.Error -> failureState.value = result.error
                    is UiState.Loading -> TODO()
                    is UiState.Success -> _textStateFlow.value = result.data
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
