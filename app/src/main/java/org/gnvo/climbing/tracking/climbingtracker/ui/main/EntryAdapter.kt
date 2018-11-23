package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.attempt_item.view.*
import org.gnvo.climbing.tracking.climbingtracker.R
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.AttemptWithDetails
import java.time.format.DateTimeFormatter
import java.util.*

class EntryAdapter : ListAdapter<AttemptWithDetails, EntryAdapter.ViewHolder>(
    EntryDiffCallback()
) {

    private var listener: OnItemClickListener? = null

    private var formatter = DateTimeFormatter.ofPattern("EEE, yyyy/MM/dd")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attempt_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItemAt(position: Int): AttemptWithDetails {
        return getItem(position)
    }

    open inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(attemptWithDetails: AttemptWithDetails) {
            itemView.text_view_grade.text = attemptWithDetails.routeGrade.french

            val listOfDetails = LinkedList<String?>()
            listOfDetails.add(attemptWithDetails.attempt.datetime.format(formatter))
            listOfDetails.add(attemptWithDetails.attempt.outcome)
            listOfDetails.add(attemptWithDetails.attempt.climbStyle)
            listOfDetails.add(attemptWithDetails.attempt.routeType)
            listOfDetails.add(attemptWithDetails.attempt.routeName)
            listOfDetails.add(attemptWithDetails.attempt.location?.area)
            listOfDetails.add(attemptWithDetails.attempt.location?.sector)
            listOfDetails.add("rating:" + attemptWithDetails.attempt.rating?.toString() + "/5")

            itemView.text_view_details.text = listOfDetails.filter{!it.isNullOrEmpty()}.joinToString()
            
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                    listener?.onItemClick(getItem(position))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(attemptWithDetails: AttemptWithDetails)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

