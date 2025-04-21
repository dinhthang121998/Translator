package com.example.ui.util

import android.view.View

fun View.showOrGone(isShowed: Boolean) {
    this.visibility = if (isShowed) View.VISIBLE else View.GONE
}
