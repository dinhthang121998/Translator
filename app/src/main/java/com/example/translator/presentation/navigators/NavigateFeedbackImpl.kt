package com.example.translator.presentation.navigators

import android.content.Context
import android.content.Intent
import com.example.feedback.FeedbackActivity
import com.example.navigation.NavigateFeedback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigateFeedbackImpl
    @Inject
    constructor() : NavigateFeedback {
        override fun navigateToFeedback(context: Context) {
            val intent = Intent(context, FeedbackActivity::class.java)
            context.startActivity(intent)
        }
    }
