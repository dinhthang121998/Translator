package com.example.translator.presentation.navigators

import android.content.Context
import android.content.Intent
import com.example.navigation.NavigateTranslateCamera
import com.example.translatecamerax.TranslateCameraXActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigateTranslateCameraImpl @Inject constructor(): NavigateTranslateCamera {
    override fun navigateToTranslateCamera(context: Context) {
        val intent = Intent(context, TranslateCameraXActivity::class.java)
        context.startActivity(intent)
    }
}