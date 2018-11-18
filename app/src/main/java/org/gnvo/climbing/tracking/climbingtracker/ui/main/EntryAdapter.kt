package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.climb_entry_item.view.*
import org.gnvo.climbing.tracking.climbingtracker.R
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntrySummary
import java.text.SimpleDateFormat
import java.util.*

class EntryAdapter : ListAdapter<ClimbEntrySummary, EntryAdapter.ViewHolder>(
    EntryDiffCallback()
) {

    private var listener: OnItemClickListener? = null

    private val dateFormater = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.climb_entry_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getClimbingEntryAt(position: Int): ClimbEntrySummary {
        return getItem(position)
    }

    open inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(climbEntryWithPitches: ClimbEntrySummary) {
            val orderedPitches = climbEntryWithPitches.pitches?.sortedWith(compareBy{it.pitchNumber})
            val maxText = climbEntryWithPitches.pitches?.sortedWith(compareBy{it.french})?.last()?.french
            val gradeText = orderedPitches?.map{it.french}?.joinToString(", ")
            itemView.text_view_grade.text = gradeText
            if (climbEntryWithPitches.pitches?.size!! > 1){
                itemView.text_view_max.text = "($maxText)"
                itemView.text_view_max.visibility = View.VISIBLE
            } else {
                itemView.text_view_max.visibility = View.GONE
            }
            itemView.text_view_date.text = dateFormater.format(climbEntryWithPitches.datetime)

            itemView.setOnClickListener {
                val position = adapterPosition
                Log.d("gnvo", "clicked $listener, $position")
                if (position != RecyclerView.NO_POSITION)
                    listener?.onItemClick(getItem(position))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(climbEntry: ClimbEntrySummary)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

