package org.gnvo.climb.tracking.climbtracker.ui.main

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.attempt_item.view.*
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithDetails
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
            itemView.text_view_climb_style.text = attemptWithDetails.climbStyle.climbStyleName
            itemView.text_view_outcome.text = attemptWithDetails.outcome.outcomeName
            itemView.text_view_route_grade.text = attemptWithDetails.routeGrade.french

            val attempt = attemptWithDetails.attempt
            val listOfDetails = LinkedList<String>()

            listOfDetails.add(attempt.datetime.format(formatter))
            listOfDetails.add(attemptWithDetails.routeType.routeTypeName!!)

            attempt.routeName?.let { listOfDetails.add(it) }
            attempt.length?.let { listOfDetails.add(it.toString() + "mts") }
            attempt.location?.area?.let { listOfDetails.add(it) }
            attempt.location?.sector?.let { listOfDetails.add(it) }
            attemptWithDetails.routeCharacteristics?.joinToString("/")?.let { listOfDetails.add(it) }
            attempt.rating?.let { listOfDetails.add("rating:" + it.toString() + "/5") }

            itemView.text_view_details.text = listOfDetails.joinToString()
            
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

