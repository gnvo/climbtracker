package org.gnvo.climbing.tracking.climbingtracker.ui.addeditentry.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.simple_recycler_view_item_1.view.*
import org.gnvo.climbing.tracking.climbingtracker.R
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.RouteGrade

class RouteGradeAdapter : RecyclerView.Adapter<RouteGradeAdapter.ViewHolder>() {

    private var selected: Int? = null
    private var items = ArrayList<RouteGrade>()

    fun setItems(items: List<RouteGrade>) {
        this.items = ArrayList(items)
        notifyDataSetChanged()
    }

    fun getSelected(): RouteGrade? {
        return selected?.let{items[it]}
    }

    fun setSelected(routeGrade: RouteGrade?) {
        Log.d("gnvog", "setting selected: $routeGrade")
        selected = items.indexOf(routeGrade)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.simple_recycler_view_item_1, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    open inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val item = items[position]
            Log.d("gnvog", "binding $item")
            itemView.text_view_1.text = item.french
            if (selected == position)
                itemView.text_view_1.setBackgroundColor(Color.LTGRAY)
            else
                itemView.text_view_1.setBackgroundColor(Color.WHITE)

            itemView.setOnClickListener {view ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Log.d("gnvog", "select change")
                    selected?.let{ notifyItemChanged(it) } //Uncheck old selection
                    selected = position
                    selected?.let{ notifyItemChanged(it) } //check new selection
                }
            }
        }
    }

}

