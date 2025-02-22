package com.example.translator.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding

abstract class ChooseLanguageView<T : ViewBinding>(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {
    abstract val viewBinding: T
    abstract var onClickFromLanguage: (() -> Unit)?
    abstract var onClickToLanguage: (() -> Unit)?
}
