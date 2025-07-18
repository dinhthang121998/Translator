package com.example.translator.presentation.navigators

import android.content.Context
import android.content.Intent
import com.example.navigation.NavigateTheme
import com.example.theme.ThemeActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigateThemeImpl
    @Inject
    constructor() : NavigateTheme {
        override fun navigateToTheme(context: Context) {
            val intent = Intent(context, ThemeActivity::class.java)
            context.startActivity(intent)
        }
    }
