package com.example.translateimage

import android.graphics.Matrix
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.common.UiState
import com.example.domain.GetPairLanguageUseCase
import com.example.domain.StorePairLanguageUseCase
import com.example.mlkit.TextRecognitionUtils
import com.example.mlkit.TranslationUtils
import com.example.model.SearchLanguageItem
import com.example.model.TextDrawing
import com.example.ui.base.BaseViewmodel
import com.example.ui.util.mapBoundingBox
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
        private val textRecognitionUtils: TextRecognitionUtils,
        private val translationUtils: TranslationUtils,
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
        fun textRecognition(
            uri: Uri,
            matrix: Matrix,
            fromLanguageCode: String,
            toLanguageCode: String,
        ) {
            viewModelScope.launch {
                this@TranslateImageViewmodel.uri = uri
                textRecognitionUtils.initRecognition(fromLanguageCode)
                val result =
                    textRecognitionUtils.recognizer(uri).map {
                        it.copy(
                            textLine = translationUtils.translate(it.textLine, fromLanguageCode, toLanguageCode),
                            rect = it.rect?.mapBoundingBox(matrix),
                        )
                    }
                _listTextDrawing.value = result
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
