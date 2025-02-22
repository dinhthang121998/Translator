package com.example.translator.util

import android.content.Context
import android.net.Uri
import com.example.translator.domain.model.TextDrawing
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class TextRecognitionUtils(private val context: Context) {
    private var textRecognizer: TextRecognizer? = null
    var inputImage: InputImage? = null

    fun initRecognition(languageCode: String) {
        textRecognizer =
            when (languageCode) {
                // When using Japanese script library
                "ja" -> TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())

                // When using Chinese script library
                "zh" -> TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

                // When using Korean script library
                "ko" -> TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

                // When using Devanagari script library
                "hi", "mr", "ne" ->
                    TextRecognition.getClient(
                        DevanagariTextRecognizerOptions.Builder().build(),
                    )

                // When using Latin script library
                else -> TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            }
    }

    suspend fun recognizer(uri: Uri) =
        suspendCoroutine<List<TextDrawing>> { continuation ->
            try {
                inputImage = InputImage.fromFilePath(context, uri)
                val listTextDrawing = mutableListOf<TextDrawing>()
                inputImage?.let {
                    textRecognizer?.process(it)
                        ?.addOnSuccessListener { visionText ->
                            for (block in visionText.textBlocks) {
                                for (line in block.lines) {
                                    val lineText = line.text
                                    val lineCornerPoints = line.cornerPoints
                                    val lineFrame = line.boundingBox
                                    val textDrawing = TextDrawing(lineText, lineFrame)
                                    listTextDrawing.add(textDrawing)
                                }
                            }
                            continuation.resume(listTextDrawing)
                        }
                        ?.addOnFailureListener { e ->
                            continuation.resumeWithException(e)
                        }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
}
