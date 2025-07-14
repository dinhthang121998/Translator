package com.example.translatecamerax

import androidx.camera.core.ImageProxy
import androidx.lifecycle.viewModelScope
import com.example.common.UiState
import com.example.domain.TranslateTextFromCameraUseCase
import com.example.model.SearchLanguageItem
import com.example.model.TextDrawing
import com.example.ui.base.BaseViewmodel
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateCameraXViewmodel
    @Inject
    constructor(
        private val translateTextFromCameraUseCase: TranslateTextFromCameraUseCase,
    ) :
    BaseViewmodel() {
        private val _textStateFlow = MutableStateFlow<List<TextDrawing>>(listOf())
        val textStateFlow: StateFlow<List<TextDrawing>> = _textStateFlow.asStateFlow()

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
                        SearchLanguageItem.LanguageItem(languageCode = "en"),
                        SearchLanguageItem.LanguageItem(languageCode = "vi"),
                    )
                when (val result = translateTextFromCameraUseCase.invoke(input)) {
                    is UiState.Error -> failureState.value = result.error
                    is UiState.Loading -> TODO()
                    is UiState.Success -> _textStateFlow.value = result.data
                }
            }
        }
    }
