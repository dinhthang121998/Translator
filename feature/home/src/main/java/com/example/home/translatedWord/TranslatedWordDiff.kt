package com.example.home.translatedWord

import androidx.recyclerview.widget.DiffUtil
import com.example.model.TranslatedWord

/**
 * If areItemsTheSame() returns false, the items are completely different entities
 *      The old item is considered removed
 *      The new item is considered inserted
 * If areItemsTheSame() returns true but areContentsTheSame() returns false:
 *      They're the same entity but with updated content
 *      DiffUtil will trigger a content update animation
 * If both methods return true:
 *      The items are identical - no change needed
 */
class TranslatedWordDiff(
    private val oldList: List<TranslatedWord>,
    private val newList: List<TranslatedWord>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        // Compare all relevant fields to see if data has changed
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.originalWord == newItem.originalWord &&
            oldItem.translatedWord == newItem.translatedWord &&
            oldItem.isFavourite == newItem.isFavourite
    }
}
