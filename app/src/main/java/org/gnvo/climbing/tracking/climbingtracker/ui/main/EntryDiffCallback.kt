package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.support.v7.util.DiffUtil
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntrySummary

class EntryDiffCallback : DiffUtil.ItemCallback<ClimbEntrySummary>() {
    override fun areItemsTheSame(oldItem: ClimbEntrySummary, newItem: ClimbEntrySummary): Boolean {
        return oldItem.climbEntryId == newItem.climbEntryId
    }

    override fun areContentsTheSame(oldItem: ClimbEntrySummary, newItem: ClimbEntrySummary): Boolean {
        return oldItem.datetime == newItem.datetime &&
                oldItem.name == newItem.name &&
                oldItem.area == newItem.area &&
                oldItem.sector == newItem.sector &&
                oldItem.rating == newItem.rating &&
                oldItem.routeType == newItem.routeType &&
                oldItem.pitches?.sortedBy { it.french } == newItem.pitches?.sortedBy { it.french }
    }
}