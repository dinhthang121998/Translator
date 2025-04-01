package com.example.translateimage

import android.net.Uri
import androidx.lifecycle.viewModelScope
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        private val translationUtils: TranslationUtils,
        private val processorMap: Map<TextRecognition, @JvmSuppressWildcards ImageProcessor>,
    ) :
    BaseViewmodel() {
        private val _listTextDrawing = MutableStateFlow<List<TextDrawing>>(listOf())
        val listTextDrawing: StateFlow<List<TextDrawing>> = _listTextDrawing

        private val _pairLanguageFlow =
            MutableStateFlow(Pair(SearchLanguageItem.LanguageItem(), SearchLanguageItem.LanguageItem()))
        val pairLanguageFlow = _pairLanguageFlow

        var uri: Uri? = null

        var fromLanguageItem = SearchLanguageItem.LanguageItem()
        var toLanguageItem = SearchLanguageItem.LanguageItem()

        // TODO add languageIdentify option
        fun processImage(
            inputImage: InputImage,
            type: TextRecognition,
        ) {
            viewModelScope.launch {
                val processor = processorMap[type]
                (processor?.processImage(inputImage) as? Text)?.let {
                    val listTextDrawing = mutableListOf<TextDrawing>()

                    for (block in it.textBlocks) {
                        for (line in block.lines) {
                            val lineText = line.text
                            val lineFrame = line.boundingBox
                            val textDrawing =
                                TextDrawing(
                                    translationUtils.translate(
                                        lineText,
                                        fromLanguageItem.languageCode,
                                        toLanguageItem.languageCode,
                                    ),
                                    lineFrame,
                                )
                            listTextDrawing.add(textDrawing)
                        }
                    }
                    _listTextDrawing.value = listTextDrawing
                }
            }
        }

        fun storeLanguageItem(
            isFromLanguageItem: Boolean,
            languageItem: SearchLanguageItem.LanguageItem,
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
    }
