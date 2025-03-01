package com.example.mlkit

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage

interface ImageProcessor {
    suspend fun processImage(inputImage: InputImage): Any?

    suspend fun processImageProxy(imageProxy: ImageProxy): Any?
}
