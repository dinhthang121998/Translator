package com.example.translator.presentation.translateImage

import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.translator.domain.model.TextDrawing
import com.example.translator.presentation.BaseViewmodel
import com.example.translator.util.TextRecognitionUtils
import com.example.translator.util.TranslationUtils
import com.example.translator.util.mapBoundingBox
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateImageViewmodel
    @Inject
    constructor(
        private val textRecognitionUtils: TextRecognitionUtils,
        private val translationUtils: TranslationUtils,
    ) :
    BaseViewmodel() {
        private val _listTextDrawing = MutableStateFlow<List<TextDrawing>>(listOf())
        val listTextDrawing: StateFlow<List<TextDrawing>> = _listTextDrawing

        fun textRecognition(
            uri: Uri,
            matrix: Matrix,
        ) {
            viewModelScope.launch {
                textRecognitionUtils.initRecognition("en")
                val result =
                    textRecognitionUtils.recognizer(uri).map {
                        it.copy(textLine = translationUtils.translate(it.textLine, "en", "vi"), rect = it.rect?.mapBoundingBox(matrix))
                    }
                _listTextDrawing.value = result
                Log.d(
                    "AAAA",
                    "Input image: height = ${textRecognitionUtils.inputImage?.height}, width = ${textRecognitionUtils.inputImage?.width}",
                )
                Log.d("AAAA", "result = $result")
            }
        }
    }
