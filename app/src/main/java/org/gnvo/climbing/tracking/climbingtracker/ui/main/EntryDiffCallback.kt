package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.support.v7.util.DiffUtil
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry

class EntryDiffCallback : DiffUtil.ItemCallback<ClimbEntry>() {
    override fun areItemsTheSame(oldItem: ClimbEntry, newItem: ClimbEntry): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ClimbEntry, newItem: ClimbEntry): Boolean {
        return oldItem.name == newItem.name &&
             oldItem.comment == newItem.comment// &&
//             oldItem.priority == newItem.priority
    }
}