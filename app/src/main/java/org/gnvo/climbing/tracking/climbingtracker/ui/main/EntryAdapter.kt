package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.climb_entry_item.view.*
import org.gnvo.climbing.tracking.climbingtracker.R
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntrySummary
import java.time.format.DateTimeFormatter
import java.util.*

class EntryAdapter : ListAdapter<ClimbEntrySummary, EntryAdapter.ViewHolder>(
    EntryDiffCallback()
) {

    private var listener: OnItemClickListener? = null

    private var formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

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
        fun bind(ClimbEntrySummary: ClimbEntrySummary) {
            val orderedPitchesWithGrades = ClimbEntrySummary.pitches?.sortedWith(compareBy{it.pitchNumber})
            val maxText = ClimbEntrySummary.pitches?.sortedWith(compareBy{it.french})?.last()?.french
            val gradeText = orderedPitchesWithGrades?.map{it.french}?.joinToString(", ")
            itemView.text_view_grade.text = gradeText
            if (ClimbEntrySummary.pitches?.size!! > 1){
                itemView.text_view_max.text = itemView.context.getString(R.string.max_message, maxText)
                itemView.text_view_max.visibility = View.VISIBLE
            } else {
                itemView.text_view_max.visibility = View.GONE
            }

            val listOfDetails = LinkedList<String?>()
            listOfDetails.add(ClimbEntrySummary.datetime.format(formatter))
            listOfDetails.add(ClimbEntrySummary.routeType)

            itemView.text_view_details.text = listOfDetails.filter{!it.isNullOrEmpty()}.joinToString()
            
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                    listener?.onItemClick(getItem(position))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(climbEntrySummary: ClimbEntrySummary)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

