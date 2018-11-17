package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.climb_entry_item.view.*
import org.gnvo.climbing.tracking.climbingtracker.R
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryWithPitches
import java.text.SimpleDateFormat
import java.util.*

class EntryAdapter : ListAdapter<ClimbEntryWithPitches, EntryAdapter.ViewHolder>(
    EntryDiffCallback()
) {

    private var listener: OnItemClickListener? = null

    //HARDCODED PREFERENCES: //TODO: Unhardcode and let the user set them
    private val dateFormater = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    private val gradesToShow = arrayOf("french", "yds", "uiaa")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.climb_entry_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getClimbingEntryAt(position: Int): ClimbEntryWithPitches {
        return getItem(position)
    }

    open inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(climbEntryWithPitches: ClimbEntryWithPitches) {
//            val orderedPitches = climbEntry.pitches.sortedWith(compareBy{it.pitchNumber})
//            val gradesText= orderedPitches.
//                    map{ it.routeGrade.french + "/" + it.routeGrade.yds }.
//                    joinToString(",")
//            val maxText = if (orderedPitches.size > 1) {
//                val pitchesOrderedByGrade = orderedPitches.sortedWith(compareBy{it.routeGrade.french})
//                "(max: ${pitchesOrderedByGrade[0].routeGrade.french}/${pitchesOrderedByGrade[0].routeGrade.yds}) "
//            } else {
//                ""
//            }
//            itemView.text_view_grade.text = maxText + gradesText
            itemView.text_view_date.text = dateFormater.format(climbEntryWithPitches.climbEntry!!.datetime)

            itemView.setOnClickListener {
                val position = adapterPosition
                Log.d("gnvo", "clicked $listener, $position")
                if (position != RecyclerView.NO_POSITION)
                    listener?.onItemClick(getItem(position))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(climbEntry: ClimbEntryWithPitches)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

