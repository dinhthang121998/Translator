package com.example.translator.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.translator.R
import com.example.translator.databinding.HomeChooseLanguageViewBinding

class HomeChooseLanguageView(context: Context, attributeSet: AttributeSet) :
    ChooseLanguageView<HomeChooseLanguageViewBinding>(context, attributeSet) {
    override val viewBinding: HomeChooseLanguageViewBinding =
        HomeChooseLanguageViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true,
        )
    override var onClickFromLanguage: (() -> Unit)? = null
    override var onClickToLanguage: (() -> Unit)? = null
    var onClickSwitch: (() -> Unit)? = null

    init {
        val typedArray = context.theme.obtainStyledAttributes(attributeSet, R.styleable.ChooseLanguageView, 0, 0)
        try {
            viewBinding.tvFromLanguage.text = typedArray.getString(R.styleable.ChooseLanguageView_fromLanguageText)
            viewBinding.tvToLanguage.text = typedArray.getString(R.styleable.ChooseLanguageView_toLanguageText)
            val drawable = typedArray.getDrawable(R.styleable.ChooseLanguageView_middleDrawable)
            drawable?.let {
                viewBinding.ivSwitch.setImageDrawable(drawable)
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
        viewBinding.ivSwitch.setOnClickListener {
            onClickSwitch?.invoke()
        }
    }

    fun setFromLanguage(languageName: String) {
        viewBinding.tvFromLanguage.text = languageName
    }

    fun setToLanguage(languageName: String) {
        viewBinding.tvToLanguage.text = languageName
    }
}
