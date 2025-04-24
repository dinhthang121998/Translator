package com.example.pronunciationspeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.PronunciationSpeed
import com.example.pronunciationspeed.databinding.PronunciationSpeedItemBinding

class PronunciationSpeedAdapter(
    private var listPronunciationSpeed: List<PronunciationSpeed>,
    val onClickItem: (PronunciationSpeed) -> Unit,
    val onSoundClick: (PronunciationSpeed) -> Unit,
) :
    RecyclerView.Adapter<PronunciationSpeedAdapter.ItemViewHolder>() {
    fun updatedListPronunciationSpeed(listPronunciationSpeed: List<PronunciationSpeed>) {
        this.listPronunciationSpeed = listPronunciationSpeed
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemViewHolder {
        val view =
            PronunciationSpeedItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPronunciationSpeed.size
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int,
    ) {
        holder.bind(listPronunciationSpeed[position])
    }

    inner class ItemViewHolder(private val binding: PronunciationSpeedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PronunciationSpeed) {
            binding.item.onItemClick = {
                onClickItem.invoke(item)
            }

            binding.item.setLabelText(item.speedType.typeName)

            binding.item.onHeaderIconClick = {
                onSoundClick.invoke(item)
            }

            binding.item.setTrailingIconVisibility(item.isSelected)
        }
    }
}
