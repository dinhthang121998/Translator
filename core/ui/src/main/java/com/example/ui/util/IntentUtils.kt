package com.example.ui.util

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.navigateToActivity(destination: Class<out AppCompatActivity>) {
    val intent = Intent(this.requireContext(), destination)
    startActivity(intent)
}

fun speechIntent(languageCode: String) =
    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
        )
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            languageCode,
        ) // Set the language
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
    }
