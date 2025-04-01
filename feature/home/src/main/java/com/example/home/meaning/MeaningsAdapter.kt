package com.example.home.meaning

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.home.databinding.DefinitionItemBinding
import com.example.home.databinding.PartOfSpeechItemBinding
import com.example.model.MeaningItem
import com.example.ui.util.showOrGone

class MeaningsAdapter(private var listMeanings: List<MeaningItem>) :
    RecyclerView.Adapter<MeaningsAdapter.MeaningViewHolder>() {

    fun updateListMeanings(listMeanings: List<MeaningItem>) {
        this.listMeanings = listMeanings
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (listMeanings[position]) {
            is MeaningItem.PartOfSpeechItem -> PART_OF_SPEECH_TYPE
            is MeaningItem.DefinitionsItem -> DEFINITION_TYPE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MeaningViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            PART_OF_SPEECH_TYPE -> {
                val view = PartOfSpeechItemBinding.inflate(inflater, parent, false)
                PartOfSpeechViewHolder(view)
            }

            DEFINITION_TYPE -> {
                val view = DefinitionItemBinding.inflate(inflater, parent, false)
                DefinitionViewHolder(view)
            }

            else -> {
                val view = PartOfSpeechItemBinding.inflate(inflater, parent, false)
                PartOfSpeechViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return listMeanings.size
    }

    override fun onBindViewHolder(
        holder: MeaningViewHolder,
        position: Int,
    ) {
        holder.bind(listMeanings[position])
    }

    abstract class MeaningViewHolder(viewBinding: ViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        abstract fun bind(meaningItem: MeaningItem)
    }

    inner class PartOfSpeechViewHolder(private val viewBinding: PartOfSpeechItemBinding) :
        MeaningViewHolder(viewBinding) {
        override fun bind(meaningItem: MeaningItem) {
            val partOfSpeechItem = meaningItem as MeaningItem.PartOfSpeechItem
            viewBinding.tvPartOfSpeech.text = partOfSpeechItem.partOfSpeech
        }
    }

    inner class DefinitionViewHolder(private val viewBinding: DefinitionItemBinding) :
        MeaningViewHolder(viewBinding) {
        override fun bind(meaningItem: MeaningItem) {
            val definitionsItem = meaningItem as MeaningItem.DefinitionsItem
            viewBinding.tvDefinition.text = definitionsItem.definitions.definition

            definitionsItem.definitions.example?.let {
                viewBinding.tvExample.showOrGone(true)
                viewBinding.tvExample.text = "\"$it\""
            } ?: run {
                viewBinding.tvExample.showOrGone(false)
                viewBinding.tvExample.text = ""
            }

            if (definitionsItem.definitions.antonyms.size != 0) {
                viewBinding.cAntonym.showOrGone(true)
                viewBinding.tvAntonym.text = definitionsItem.definitions.antonyms.joinToString(", ")
            } else {
                viewBinding.cAntonym.showOrGone(false)
                viewBinding.tvAntonym.text = ""
            }

            if (definitionsItem.definitions.synonyms.size != 0) {
                viewBinding.cSynonym.showOrGone(true)
                viewBinding.tvsynonyms.text = definitionsItem.definitions.synonyms.joinToString(", ")
            } else {
                viewBinding.cSynonym.showOrGone(false)
                viewBinding.tvsynonyms.text = ""
            }
        }
    }

    companion object {
        const val PART_OF_SPEECH_TYPE = 0
        const val DEFINITION_TYPE = 1
    }
}
