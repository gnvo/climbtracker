package org.gnvo.climb.tracking.climbtracker.ui.main.views

import android.support.v7.widget.RecyclerView
import android.view.View
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptListItem
import org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter.EntryAdapter

abstract class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: AttemptListItem, listener: EntryAdapter.OnItemClickListener?)
}