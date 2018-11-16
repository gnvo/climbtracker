package tracking.climbing.gnvo.org.climbingtracker.ui.main

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.climb_entry_item.view.*
import tracking.climbing.gnvo.org.climbingtracker.R
import tracking.climbing.gnvo.org.climbingtracker.data.room.pojo.ClimbEntry
import java.text.SimpleDateFormat
import java.util.*

class EntryAdapter : ListAdapter<ClimbEntry, EntryAdapter.ViewHolder>(EntryDiffCallback()) {

    private var listener: OnItemClickListener? = null

    private val dateFormater = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.climb_entry_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getClimbingEntryAt(position: Int): ClimbEntry {
        return getItem(position)
    }

    open inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(climbEntry: ClimbEntry) {
            val orderedPitches = climbEntry.pitches.sortedWith(compareBy{it.pitchNumber})
            val gradesText = when (orderedPitches.size) {
                1 -> orderedPitches[0].routeGradeId.toString()
                else -> {
                    val grades= orderedPitches.map{it.routeGradeId!!}
                    "${grades.joinToString(",")} (max: ${grades.max()})"
                }
            }
            itemView.text_view_grade.text = gradesText
            itemView.text_view_date.text = dateFormater.format(climbEntry.datetime)

            itemView.setOnClickListener {
                val position = adapterPosition
                Log.d("gnvo", "clicked $listener, $position")
                if (position != RecyclerView.NO_POSITION)
                    listener?.onItemClick(getItem(position))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(climbEntry: ClimbEntry)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

