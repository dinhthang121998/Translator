package com.example.home.translatedWord

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.home.databinding.TranslatedWordItemBinding
import com.example.model.TranslatedWord

class TranslatedWordAdapter(
    private var translatedWords: List<TranslatedWord>,
    val onClickFavorite: ((TranslatedWord) -> Unit)? = null,
    val onClickItem: ((TranslatedWord) -> Unit)? = null,
) :
    RecyclerView.Adapter<TranslatedWordAdapter.TranslatedWordViewHolder>() {
    fun updateTranslatedWords(translatedWords: List<TranslatedWord>) {
        val diffCallback = TranslatedWordDiff(this.translatedWords, translatedWords)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.translatedWords = translatedWords
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TranslatedWordViewHolder {
        val view =
            TranslatedWordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TranslatedWordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return translatedWords.size
    }

    override fun onBindViewHolder(
        holder: TranslatedWordViewHolder,
        position: Int,
    ) {
        holder.bind(translatedWords[position])
    }

    inner class TranslatedWordViewHolder(private val viewBinding: TranslatedWordItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(translatedWord: TranslatedWord) {
            viewBinding.tvOriginalWord.text = translatedWord.originalWord
            viewBinding.tvTranslatedWord.text = translatedWord.translatedWord
            viewBinding.ivIsFavorite.isSelected = translatedWord.isFavourite
            viewBinding.ivIsFavorite.setOnClickListener {
                onClickFavorite?.invoke(translatedWord)
            }
            viewBinding.cTranslatedWord.setOnClickListener {
                onClickItem?.invoke(translatedWord)
            }
        }
    }
}
