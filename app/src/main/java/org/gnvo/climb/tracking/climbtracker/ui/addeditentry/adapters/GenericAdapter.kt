package org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.simple_recycler_view_item_1.view.*
import org.gnvo.climb.tracking.climbtracker.R

abstract class GenericAdapter<T> : RecyclerView.Adapter<GenericAdapter<T>.ViewHolder>() {

    internal var items = ArrayList<T>()
    private var formatter: CustomFormatter<T>? = null

    open fun setItems(items: List<T>) {
        this.items = ArrayList(items)
        notifyDataSetChanged()
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
        open fun bind(position: Int) {
            val item = items[position]
            when (formatter){
                null -> itemView.text_view_1.text = item.toString()
                else -> itemView.text_view_1.text = formatter?.format(item)
            }
            customBind(itemView, position)
        }
    }

    interface CustomFormatter<T> {
        fun format(item: T): String
    }

    fun setCustomFormatter(formatter: CustomFormatter<T>){
        this.formatter = formatter
    }

    abstract fun customBind(itemView: View, position: Int)
}

