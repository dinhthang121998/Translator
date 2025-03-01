package com.example.mlkit.textRecognition

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.example.mlkit.ImageProcessorBase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextRecognitionProcessor
    @Inject
    constructor(private val optionsInterface: TextRecognizerOptionsInterface) :
    ImageProcessorBase() {
        private val textProcessor = TextRecognition.getClient(optionsInterface)

        override suspend fun processImage(inputImage: InputImage): Any =
            suspendCoroutine { continuation ->
                textProcessor.process(inputImage).addOnSuccessListener { texts ->
                    continuation.resume(texts)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
            }

        @OptIn(ExperimentalGetImage::class)
        override suspend fun processImageProxy(imageProxy: ImageProxy): Any? =
            suspendCoroutine { continuation ->
                val inputImage =
                    InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
                textProcessor.process(inputImage).addOnSuccessListener { texts ->
                    continuation.resume(texts)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }.addOnCompleteListener {
                    // When the image is from CameraX analysis use case, must call image.close() on received
                    // images when finished using them. Otherwise, new images may not be received or the camera
                    // may stall.
                    imageProxy.close()
                }
            }
    }
