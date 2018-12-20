package org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptHeader
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptListItem
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithLocationAndGrades
import org.gnvo.climb.tracking.climbtracker.ui.main.views.ViewHolder
import org.gnvo.climb.tracking.climbtracker.ui.main.views.ViewHolderAttempt
import org.gnvo.climb.tracking.climbtracker.ui.main.views.ViewHolderHeader

class EntryAdapter(internal var alwaysShowGrade: String?) : ListAdapter<AttemptListItem, ViewHolder>(
    EntryDiffCallback()
) {
    companion object {
        private const val TYPE_HEADER = 1
        private const val TYPE_ITEM = 2
    }

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType){
            TYPE_HEADER -> ViewHolderHeader(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_attempt_header, parent, false))
            else -> ViewHolderAttempt(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_attempt_item, parent, false), alwaysShowGrade)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
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
            is AttemptWithLocationAndGrades -> TYPE_ITEM
            is AttemptHeader -> TYPE_HEADER
            else -> 0
        }
    }
}