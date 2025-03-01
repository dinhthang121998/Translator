package com.example.translatecamerax

import androidx.camera.core.ImageProxy
import androidx.lifecycle.viewModelScope
import com.example.mlkit.ImageProcessor
import com.example.mlkit.TextRecognition
import com.example.mlkit.utils.TranslationUtils
import com.example.model.TextDrawing
import com.example.ui.base.BaseViewmodel
import com.google.mlkit.vision.text.Text
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateCameraXViewmodel
    @Inject
    constructor(
        private val translationUtils: TranslationUtils,
        private val processorMap: Map<TextRecognition, @JvmSuppressWildcards ImageProcessor>,
    ) :
    BaseViewmodel() {
        private val _textStateFlow = MutableStateFlow<List<TextDrawing>>(listOf())
        val textStateFlow = _textStateFlow

        fun processImageProxy(
            inputImage: ImageProxy,
            type: TextRecognition,
        ) {
            viewModelScope.launch {
                val processor = processorMap[type]
                (processor?.processImageProxy(inputImage) as? Text)?.let {
                    val listTextDrawing = mutableListOf<TextDrawing>()
                    for (block in it.textBlocks) {
                        for (line in block.lines) {
                            val lineText = line.text
                            val lineFrame = line.boundingBox
                            // TODO: Add selecting language
                            val textDrawing =
                                TextDrawing(
                                    translationUtils.translate(
                                        lineText,
                                        "en",
                                        "vi",
                                    ),
                                    lineFrame,
                                )
                            listTextDrawing.add(textDrawing)
                        }
                    }
                    _textStateFlow.value = listTextDrawing
                }
            }
        }
    }
