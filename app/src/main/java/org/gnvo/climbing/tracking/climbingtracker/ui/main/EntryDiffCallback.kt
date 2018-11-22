package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.support.v7.util.DiffUtil
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.AttemptWithDetails

class EntryDiffCallback : DiffUtil.ItemCallback<AttemptWithDetails>() {
    override fun areItemsTheSame(oldItem: AttemptWithDetails, newItem: AttemptWithDetails): Boolean {
        return oldItem.attempt.id == newItem.attempt.id
    }

    override fun areContentsTheSame(oldItem: AttemptWithDetails, newItem: AttemptWithDetails): Boolean {
        return oldItem.equals(newItem)
    }
}