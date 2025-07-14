package com.example.mlkit

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface

interface ImageProcessor {
    suspend fun processImage(
        optionsInterface: TextRecognizerOptionsInterface,
        inputImage: InputImage,
    ): Text?

    suspend fun processImageProxy(
        optionsInterface: TextRecognizerOptionsInterface,
        imageProxy: ImageProxy,
    ): Text?
}
