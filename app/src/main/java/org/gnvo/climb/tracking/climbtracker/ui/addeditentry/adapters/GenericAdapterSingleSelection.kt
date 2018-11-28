package org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.simple_recycler_view_item_1.view.*

class GenericAdapterSingleSelection<T> : GenericAdapter<T>() {
    private var selectedPos: Int = RecyclerView.NO_POSITION
    private var selectedItem: T? = null
    private var scroller: Scroller? = null

    override fun setItems(items: List<T>) {
        super.setItems(items)
        selectedPos = items.indexOf(selectedItem)
        scroller?.scroll(selectedPos)
    }

    fun getSelected(): T? {
        return if (selectedPos == RecyclerView.NO_POSITION || items.isEmpty()) null
        else {
            items[selectedPos]
        }
    }

    fun setSelected(item: T?) {
        selectedItem = item
        if (items.isNotEmpty()) {
            with(items.indexOf(selectedItem)) {
                selectedPos = this
                scroller?.scroll(this)
                notifyItemChanged(this)
            }
        }
    }

    interface Scroller {
        fun scroll(selectedPosition: Int)
    }

    fun setScroller(scroller: Scroller) {
        this.scroller = scroller
    }

    override fun customBind(itemView: View, position: Int, adapterPosition: Int) {
        when (position) {
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