package org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recycler_view_item_1.view.*
import org.gnvo.climb.tracking.climbtracker.R

abstract class GenericAdapter : RecyclerView.Adapter<GenericAdapter.ViewHolder>() {

    internal var items = ArrayList<String>()

    open fun setItems(items: ArrayList<String>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_1, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    open inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(position: Int) {
            val item = items[position]
            itemView.text_view_1.text = item.toString()
            customBind(itemView, position)
        }
    }

    abstract fun customBind(itemView: View, position: Int)
}

