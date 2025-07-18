package com.example.translator.presentation.navigators

import android.content.Context
import android.content.Intent
import com.example.navigation.NavigateTranslateImage
import com.example.translateimage.TranslateImageActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigateTranslateImageImpl
    @Inject
    constructor() : NavigateTranslateImage {
        override fun navigateToTranslateImage(context: Context) {
            val intent = Intent(context, TranslateImageActivity::class.java)
            context.startActivity(intent)
        }
    }
