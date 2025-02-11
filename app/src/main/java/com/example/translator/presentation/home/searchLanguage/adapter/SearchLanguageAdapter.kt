package com.example.translator.presentation.home.searchLanguage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.translator.R
import com.example.translator.databinding.SearchLanguageItemBinding
import com.example.translator.databinding.SearchLanguageTitleItemBinding
import com.example.translator.domain.model.Downloadable
import com.example.translator.domain.model.SearchLanguageItem

class SearchLanguageAdapter(
    private var listSearchLanguageItem: List<SearchLanguageItem>,
    val onClickItem: ((SearchLanguageItem.LanguageItem) -> Unit)? = null,
) :
    RecyclerView.Adapter<SearchLanguageAdapter.SearchLanguageViewHolder>() {
    fun updateListLanguage(listSearchLanguageItem: List<SearchLanguageItem>) {
        this.listSearchLanguageItem = listSearchLanguageItem
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (listSearchLanguageItem[position]) {
            is SearchLanguageItem.LanguageItem -> LANGUAGE_TYPE
            is SearchLanguageItem.TitleItem -> TITLE_TYPE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchLanguageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TITLE_TYPE -> {
                val view = SearchLanguageTitleItemBinding.inflate(inflater, parent, false)
                TitleViewHolder(view)
            }

            LANGUAGE_TYPE -> {
                val view = SearchLanguageItemBinding.inflate(inflater, parent, false)
                LanguageViewHolder(view)
            }

            else -> {
                val view = SearchLanguageTitleItemBinding.inflate(inflater, parent, false)
                TitleViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return listSearchLanguageItem.size
    }

    override fun onBindViewHolder(
        holder: SearchLanguageViewHolder,
        position: Int,
    ) {
        holder.bind(listSearchLanguageItem[position])
    }

    abstract class SearchLanguageViewHolder(viewBinding: ViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        abstract fun bind(item: SearchLanguageItem)
    }

    inner class TitleViewHolder(private val viewBinding: SearchLanguageTitleItemBinding) :
        SearchLanguageViewHolder(viewBinding) {
        override fun bind(item: SearchLanguageItem) {
            val title = item as SearchLanguageItem.TitleItem
            viewBinding.tvTitle.text = title.title
        }
    }

    inner class LanguageViewHolder(private val viewBinding: SearchLanguageItemBinding) :
        SearchLanguageViewHolder(viewBinding) {
        override fun bind(item: SearchLanguageItem) {
            val language = item as SearchLanguageItem.LanguageItem
            viewBinding.tvLanguage.text = language.languageName
            when (language.isDownload) {
                Downloadable.IS_DOWNLOADED -> viewBinding.ivDownload.setImageResource(R.drawable.ic_downloaded)
                Downloadable.NEED_DOWNLOAD -> viewBinding.ivDownload.setImageResource(R.drawable.ic_download)
                Downloadable.NO_NEED_DOWNLOAD -> viewBinding.ivDownload.visibility = View.GONE
            }
            viewBinding.cLayout.setOnClickListener { onClickItem?.invoke(language) }
        }
    }

    companion object {
        const val TITLE_TYPE = 0
        const val LANGUAGE_TYPE = 1
    }
}
