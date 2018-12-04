package org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithGrades
import org.gnvo.climb.tracking.climbtracker.ui.main.views.ViewHolder
import org.gnvo.climb.tracking.climbtracker.ui.main.views.ViewHolderAttempt

class EntryAdapter : ListAdapter<AttemptWithGrades, ViewHolder>(
    EntryDiffCallback()
) {
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolderAttempt(LayoutInflater.from(parent.context).inflate(R.layout.attempt_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    fun getItemAt(position: Int): AttemptWithGrades {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onItemClick(attemptWithGrades: AttemptWithGrades)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

