package com.example.ui.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.navigateToActivity(destination: Class<out AppCompatActivity>) {
    val intent = Intent(this.requireContext(), destination)
    startActivity(intent)
}
