package com.example.ui.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.ui.R
import com.example.ui.databinding.CustomToolbarViewBinding

class CustomToolbarView(context: Context, attributes: AttributeSet) : FrameLayout(context, attributes) {
    private val binding = CustomToolbarViewBinding.inflate(LayoutInflater.from(context), this, true)

    var onBackAreaClick: (() -> Unit)? = null

    init {
        val typedArray = context.obtainStyledAttributes(attributes, R.styleable.CustomToolbarView)
        try {
            val title = typedArray.getString(R.styleable.CustomToolbarView_title)

            val headerIcon = typedArray.getResourceId(R.styleable.CustomToolbarView_headerIcon, 0)
            val headerDrawable = if (headerIcon != 0) ContextCompat.getDrawable(context, headerIcon) else null
            val headerText = typedArray.getString(R.styleable.CustomToolbarView_headerText)

            binding.backArea.text = headerText
            binding.backArea.setCompoundDrawablesRelativeWithIntrinsicBounds(headerDrawable, null, null, null)
            binding.title.text = title
            binding.backArea.setOnClickListener {
                onBackAreaClick?.invoke()
            }
        } finally {
            typedArray.recycle()
        }
    }
}
