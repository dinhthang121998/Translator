package com.example.ui.translationHistory

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.model.TranslationHistory
import com.example.ui.databinding.HistoryTranslationViewBinding

class TranslationHistoryView(context: Context, attributes: AttributeSet) :
    FrameLayout(context, attributes) {
    private val binding =
        HistoryTranslationViewBinding.inflate(LayoutInflater.from(context), this, true)

    private val historyTranslationAdapter = TranslationHistoryAdapter(listOf())

    var iHistoryTranslationAdapter: TranslationHistoryAdapter.IHistoryTranslationAdapterListener? =
        null
        set(value) {
            field = value
            historyTranslationAdapter.setListener(value) // Pass directly to adapter
        }

    init {
        binding.rcvHistoryTranslation.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyTranslationAdapter

            // Add swipe-to-delete functionality
            ItemTouchHelper(createTranslatedWordSwipeCallback()).attachToRecyclerView(this)
        }
    }

    private fun createTranslatedWordSwipeCallback() =
        object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = false

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int,
            ) {
                val position = viewHolder.adapterPosition
                val translatedWord = historyTranslationAdapter.getItemAt(position)

                translatedWord.let {
                    iHistoryTranslationAdapter?.onDeleteHistoryItem(it)
                }
            }
        }

    fun updateHistoryTranslation(translatedWords: List<TranslationHistory>) {
        historyTranslationAdapter.updateHistoryTranslation(translatedWords)
    }
}
