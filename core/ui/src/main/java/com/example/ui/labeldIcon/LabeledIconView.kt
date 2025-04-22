package com.example.ui.labeldIcon

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.ui.R
import com.example.ui.databinding.LabeledIconViewBinding

class LabeledIconView(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {

    private val binding = LabeledIconViewBinding.inflate(LayoutInflater.from(context), this, true)
    private val defaultBackgroundColor = R.color.white
    private val defaultLabelColor = R.color.black

    var onItemClick: () -> Unit = {}

    init {
        val typedArray =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.LabeledIconView,
                0,
                0,
            )

        try {
            handleAttributeSets(typedArray)
            binding.root.setOnClickListener {
                onItemClick()
            }
        } finally {
            typedArray.recycle()
        }
    }

    private fun handleAttributeSets(typedArray: TypedArray) {
        typedArray.apply {
            val labelText = getString(R.styleable.LabeledIconView_labelText)
            val trailingText = getString(R.styleable.LabeledIconView_trailingText)
            val resourceId = getResourceId(R.styleable.LabeledIconView_trailingIcon, 0)
            val backgroundColor = getResourceId(R.styleable.LabeledIconView_customBackgroundColor, defaultBackgroundColor)
            val labelColor = getResourceId(R.styleable.LabeledIconView_customLabelColor, defaultLabelColor)

            binding.labelText.text = labelText
            binding.trailingText.text = trailingText
            binding.labelText.setTextColor(ContextCompat.getColor(context, labelColor))
            binding.trailingIcon.setImageResource(resourceId)
            binding.root.setBackgroundResource(backgroundColor)
        }
    }
}