package com.example.domain

import com.example.domain.base.SuspendUseCase
import com.example.mlkit.ImageProcessor
import com.example.mlkit.TextRecognition
import com.example.mlkit.utils.TranslationUtils
import com.example.model.SearchLanguageItem
import com.example.model.TextDrawing
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TranslateTextFromImageUseCase @Inject constructor (
    private val imageProcessor: ImageProcessor,
    private val translationUtils: TranslationUtils,
    coroutineDispatcher: CoroutineDispatcher,
) :
    SuspendUseCase<TranslateTextFromImageUseCase.TranslateTextFromImageInput, List<TextDrawing>>(
        coroutineDispatcher
    ) {
    override suspend fun execute(parameter: TranslateTextFromImageInput): List<TextDrawing> {
        val text = imageProcessor.processImage(parameter.optionsInterface, parameter.inputImage)
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

    data class TranslateTextFromImageInput(
        val inputImage: InputImage,
        val optionsInterface: TextRecognizerOptionsInterface,
        val fromLanguageItem: SearchLanguageItem.LanguageItem,
        val toLanguageItem: SearchLanguageItem.LanguageItem,
    )
}