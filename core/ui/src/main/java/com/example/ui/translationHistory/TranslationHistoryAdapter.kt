package com.example.ui.translationHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.model.TranslationHistory
import com.example.ui.databinding.HistoryTranslationItemBinding

class TranslationHistoryAdapter(
    private var historyTranslation: List<TranslationHistory> = emptyList(),
) :
    RecyclerView.Adapter<TranslationHistoryAdapter.TranslatedWordViewHolder>() {
    var iHistoryTranslationAdapterListener: IHistoryTranslationAdapterListener? = null

    fun updateHistoryTranslation(historyTranslation: List<TranslationHistory>) {
        val diffCallback = TranslationHistoryDiff(this.historyTranslation, historyTranslation)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.historyTranslation = historyTranslation
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItemAt(position: Int): TranslationHistory {
        return historyTranslation[position]
    }

    fun setListener(iHistoryTranslationAdapterListener: IHistoryTranslationAdapterListener?) {
        this.iHistoryTranslationAdapterListener = iHistoryTranslationAdapterListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TranslatedWordViewHolder {
        val view =
            HistoryTranslationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TranslatedWordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyTranslation.size
    }

    override fun onBindViewHolder(
        holder: TranslatedWordViewHolder,
        position: Int,
    ) {
        holder.bind(historyTranslation[position])
    }

    inner class TranslatedWordViewHolder(private val viewBinding: HistoryTranslationItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(translatedWord: TranslationHistory) {
            viewBinding.tvOriginalWord.text = translatedWord.originalWord
            viewBinding.tvTranslatedWord.text = translatedWord.translatedWord
            viewBinding.ivIsFavorite.isSelected = translatedWord.isFavourite
            viewBinding.ivIsFavorite.setOnClickListener {
                iHistoryTranslationAdapterListener?.onClickFavorite(translatedWord)
            }
            viewBinding.cTranslatedWord.setOnClickListener {
                iHistoryTranslationAdapterListener?.onClickItem(translatedWord)
            }
        }
    }

    interface IHistoryTranslationAdapterListener {
        fun onClickFavorite(translatedWord: TranslationHistory)

        fun onClickItem(translatedWord: TranslationHistory)

        fun onDeleteHistoryItem(translatedWord: TranslationHistory)
    }
}
