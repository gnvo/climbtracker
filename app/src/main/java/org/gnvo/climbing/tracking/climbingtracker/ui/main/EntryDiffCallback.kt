package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.support.v7.util.DiffUtil
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryWithPitches

class EntryDiffCallback : DiffUtil.ItemCallback<ClimbEntryWithPitches>() {
    override fun areItemsTheSame(oldItem: ClimbEntryWithPitches, newItem: ClimbEntryWithPitches): Boolean {
        return oldItem.climbEntry!!.id == newItem.climbEntry!!.id
    }

    override fun areContentsTheSame(oldItem: ClimbEntryWithPitches, newItem: ClimbEntryWithPitches): Boolean {
        return oldItem.climbEntry!!.name == newItem.climbEntry!!.name &&
                oldItem.climbEntry!!.coordinates == newItem.climbEntry!!.coordinates &&
                oldItem.climbEntry!!.site == newItem.climbEntry!!.site &&
                oldItem.climbEntry!!.sector == newItem.climbEntry!!.sector &&
                oldItem.climbEntry!!.datetime == newItem.climbEntry!!.datetime &&
                oldItem.climbEntry!!.routeType == newItem.climbEntry!!.routeType &&
                oldItem.climbEntry!!.rating == newItem.climbEntry!!.rating &&
                oldItem.climbEntry!!.comment == newItem.climbEntry!!.comment
    }
}