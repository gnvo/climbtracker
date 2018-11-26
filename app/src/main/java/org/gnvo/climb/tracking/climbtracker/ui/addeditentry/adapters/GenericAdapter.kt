package org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.simple_recycler_view_item_1.view.*
import org.gnvo.climb.tracking.climbtracker.R

class GenericAdapter<T> : RecyclerView.Adapter<GenericAdapter<T>.ViewHolder>() {

    private var selectedPos: Int = RecyclerView.NO_POSITION
    private var selectedItem: T? = null
    private var items = ArrayList<T>()
    private var formatter: CustomFormatter<T>? = null
    private var scroller: Scroller? = null

    fun setItems(items: List<T>) {
        this.items = ArrayList(items)
        selectedPos = items.indexOf(selectedItem)
        scroller?.scroll(selectedPos)
        notifyDataSetChanged()
    }

    fun getSelected(): T? {
        return if (selectedPos == RecyclerView.NO_POSITION || items.isEmpty()) null
            else { items[selectedPos] }
    }

    fun setSelected(item: T?) {
        selectedItem = item
        if (items.isNotEmpty()) {
            with (items.indexOf(selectedItem)) {
                selectedPos = this
                scroller?.scroll(this)
                notifyItemChanged(this)
            }
        }
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
            when (formatter){
                null -> itemView.text_view_1.text = item.toString()
                else -> itemView.text_view_1.text = formatter?.format(item)
            }
            when (position){
                selectedPos -> itemView.text_view_1.setBackgroundColor(Color.LTGRAY)
                else -> itemView.text_view_1.setBackgroundColor(Color.TRANSPARENT)//TODO: this is a hack, better color handling
            }

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPos) //Uncheck old selection
                    selectedPos = position
                    notifyItemChanged(selectedPos) //check new selection
                }
            }
        }
    }

    interface CustomFormatter<T> {
        fun format(item: T): String
    }

    fun setCustomFormatter(formatter: CustomFormatter<T>){
        this.formatter = formatter
    }

    interface Scroller {
        fun scroll(selectedPosition: Int)
    }

    fun setScroller(scroller: Scroller){
        this.scroller = scroller
    }

}

