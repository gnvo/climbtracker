package org.gnvo.climb.tracking.climbtracker.ui.main.views

import android.support.v7.widget.RecyclerView
import android.view.View
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithGrades
import org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter.EntryAdapter

abstract class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(attemptWithGrades: AttemptWithGrades, listener: EntryAdapter.OnItemClickListener?)
}