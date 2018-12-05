package org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptHeader
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptListItem
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithGrades
import org.gnvo.climb.tracking.climbtracker.ui.main.views.ViewHolder
import org.gnvo.climb.tracking.climbtracker.ui.main.views.ViewHolderAttempt
import org.gnvo.climb.tracking.climbtracker.ui.main.views.ViewHolderHeader

class EntryAdapter : ListAdapter<AttemptListItem, ViewHolder>(
    EntryDiffCallback()
) {
    private var listener: OnItemClickListener? = null

    val TYPE_HEADER = 1
    val TYPE_ITEM = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType){
            TYPE_HEADER -> ViewHolderHeader(LayoutInflater.from(parent.context).inflate(R.layout.attempt_header, parent, false))
            else -> ViewHolderAttempt(LayoutInflater.from(parent.context).inflate(R.layout.attempt_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    fun getItemAt(position: Int): AttemptListItem {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onItemClick(attemptListItem: AttemptListItem)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is AttemptWithGrades -> TYPE_ITEM
            is AttemptHeader -> TYPE_HEADER
            else -> 0
        }
    }
}