package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.support.v7.util.DiffUtil
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryFull

class EntryDiffCallback : DiffUtil.ItemCallback<ClimbEntryFull>() {
    override fun areItemsTheSame(oldItem: ClimbEntryFull, newItem: ClimbEntryFull): Boolean {
        return oldItem.climbEntryId == newItem.climbEntryId
    }

    override fun areContentsTheSame(oldItem: ClimbEntryFull, newItem: ClimbEntryFull): Boolean {
        return oldItem.datetime == newItem.datetime &&
                oldItem.pitches?.sortedBy { it.french } == newItem.pitches?.sortedBy { it.french }
    }
}