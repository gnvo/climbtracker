package org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter

import android.support.v7.util.DiffUtil
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithGrades

class EntryDiffCallback : DiffUtil.ItemCallback<AttemptWithGrades>() {
    override fun areItemsTheSame(oldItem: AttemptWithGrades, newItem: AttemptWithGrades): Boolean {
        return oldItem.attempt.id == newItem.attempt.id
    }

    override fun areContentsTheSame(oldItem: AttemptWithGrades, newItem: AttemptWithGrades): Boolean {
        return oldItem == newItem
    }
}