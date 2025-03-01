package com.example.mlkit.di

import com.example.mlkit.TextRecognition
import dagger.MapKey

@MapKey
annotation class TextRecognitionKey(val type: TextRecognition)
