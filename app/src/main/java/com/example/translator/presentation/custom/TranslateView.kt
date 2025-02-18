package com.example.translator.presentation.custom

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.widget.addTextChangedListener
import com.example.translator.R
import com.example.translator.databinding.TranslateViewBinding
import com.example.translator.util.showOrGone

class TranslateView(context: Context, attributes: AttributeSet) : FrameLayout(context, attributes) {
    private val binding: TranslateViewBinding by lazy {
        TranslateViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    var onClickClose: (() -> Unit?)? = null
    var onFocusListener: ((Boolean) -> Unit)? = null
    var onTextChanged: ((String) -> Unit)? = null
    var onClickTranslation: (() -> Unit)? = null
    var onClickSpeak: ((String) -> Unit)? = null
    var onClickMic: (() -> Unit)? = null

    init {
        binding.lottieLoading.setAnimation(R.raw.loading)
        val typedArray = context.theme.obtainStyledAttributes(attributes, R.styleable.TranslateView, 0, 0)

        try {
            binding.editText.hint = typedArray.getString(R.styleable.TranslateView_hint)
            binding.editText.setOnFocusChangeListener { v, hasFocus ->
                onFocusListener?.invoke(hasFocus)
            }
            binding.editText.addTextChangedListener { text: Editable? ->
                onTextChanged?.invoke(text.toString())
            }
            binding.ivClearText.setOnClickListener {
                onClickClose?.invoke()
            }
            binding.ivTranslate.setOnClickListener {
                onClickTranslation?.invoke()
            }
            binding.ivSpeak.setOnClickListener {
                onClickSpeak?.invoke(binding.editText.text.toString())
            }
            binding.ivMic.setOnClickListener {
                onClickMic?.invoke()
            }
        } finally {
            typedArray.recycle()
        }
    }

    fun playAnimation(isPlayed: Boolean) {
        if (isPlayed) {
            binding.lottieLoading.playAnimation()
        } else {
            binding.lottieLoading.pauseAnimation()
        }
    }

    fun setReadOnly(
        value: Boolean,
        inputType: Int = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE,
    ) {
        binding.editText.isEnabled = !value
        binding.editText.isClickable = !value
        binding.editText.isFocusable = !value
        binding.editText.isFocusableInTouchMode = !value
        binding.editText.setHorizontallyScrolling(false)
        binding.editText.inputType = inputType
        binding.editText.setLines(3)
    }

    fun setText(text: String) {
        binding.editText.setText(text)
    }

    fun setUpTranslatedView() {
        binding.ivTranslate.showOrGone(true)
    }

    fun showClearIcon(isShowed: Boolean) {
        binding.ivClearText.showOrGone(isShowed)
    }

    fun showPhonetic(isShowed: Boolean) {
        binding.tvPhonetic.showOrGone(isShowed)
    }

    fun setPhonetic(phonetic: String) {
        binding.tvPhonetic.text = phonetic
    }

    fun showSpeak(isShowed: Boolean) {
        binding.ivSpeak.showOrGone(isShowed)
    }

    fun showMic(isShowed: Boolean) {
        binding.ivMic.showOrGone(isShowed)
    }
}
