package com.example.translator.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.translator.R
import com.example.translator.databinding.MediaChooseLanguageViewBinding

class MediaChooseLanguageView(context: Context, attributeSet: AttributeSet) :
    ChooseLanguageView<MediaChooseLanguageViewBinding>(context, attributeSet) {
    override val viewBinding: MediaChooseLanguageViewBinding =
        MediaChooseLanguageViewBinding.inflate(LayoutInflater.from(context), this, true)
    override var onClickFromLanguage: (() -> Unit)? = null
    override var onClickToLanguage: (() -> Unit)? = null

    init {
        val typedArray = context.theme.obtainStyledAttributes(attributeSet, R.styleable.ChooseLanguageView, 0, 0)
        try {
            viewBinding.tvFromLanguage.text = typedArray.getString(R.styleable.ChooseLanguageView_fromLanguageText)
            viewBinding.tvToLanguage.text = typedArray.getString(R.styleable.ChooseLanguageView_toLanguageText)
            val drawable = typedArray.getDrawable(R.styleable.ChooseLanguageView_middleDrawable)
            drawable?.let {
                viewBinding.ivRight.setImageDrawable(drawable)
            }
        } finally {
            typedArray.recycle()
        }
        viewBinding.tvFromLanguage.setOnClickListener {
            onClickFromLanguage?.invoke()
        }
        viewBinding.tvToLanguage.setOnClickListener {
            onClickToLanguage?.invoke()
        }
    }

    fun setFromLanguage(languageName: String) {
        viewBinding.tvFromLanguage.text = languageName
    }

    fun setToLanguage(languageName: String) {
        viewBinding.tvToLanguage.text = languageName
    }
}
