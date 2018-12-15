package org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter

import android.support.v7.util.DiffUtil
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptListItem

class EntryDiffCallback : DiffUtil.ItemCallback<AttemptListItem>() {
    override fun areItemsTheSame(oldItem: AttemptListItem, newItem: AttemptListItem): Boolean {
        return oldItem.itemsAreEqualOrHaveSameId(newItem)
    }

    override fun areContentsTheSame(oldItem: AttemptListItem, newItem: AttemptListItem): Boolean {
        return oldItem == newItem
    }
}