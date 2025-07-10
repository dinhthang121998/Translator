package com.example.domain

import androidx.camera.core.ImageProxy
import com.example.domain.base.SuspendUseCase
import com.example.mlkit.ImageProcessor
import com.example.mlkit.utils.TranslationUtils
import com.example.model.SearchLanguageItem
import com.example.model.TextDrawing
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TranslateTextFromCameraUseCase @Inject constructor (
    private val imageProcessor: ImageProcessor,
    private val translationUtils: TranslationUtils,
    coroutineDispatcher: CoroutineDispatcher,
): SuspendUseCase<TranslateTextFromCameraUseCase.TranslateTextFromCameraInput, List<TextDrawing>>(
coroutineDispatcher
) {
    override suspend fun execute(parameter: TranslateTextFromCameraInput): List<TextDrawing> {
        val text = imageProcessor.processImageProxy(parameter.optionsInterface, parameter.imageProxy)
        val listTextDrawing = mutableListOf<TextDrawing>()
        text?.let {
            for (block in it.textBlocks) {
                for (line in block.lines) {
                    val lineText = line.text
                    val lineFrame = line.boundingBox
                    val textDrawing =
                        TextDrawing(
                            translationUtils.translate(
                                lineText,
                                parameter.fromLanguageItem.languageCode,
                                parameter.toLanguageItem.languageCode,
                            ),
                            lineFrame,
                        )
                    listTextDrawing.add(textDrawing)
                }
            }
        }
        return listTextDrawing
    }

    data class TranslateTextFromCameraInput(
        val imageProxy: ImageProxy,
        val optionsInterface: TextRecognizerOptionsInterface,
        val fromLanguageItem: SearchLanguageItem.LanguageItem,
        val toLanguageItem: SearchLanguageItem.LanguageItem,
    )
}