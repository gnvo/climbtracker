package org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.simple_recycler_view_item_1.view.*

class GenericAdapterSingleSelection<T> : GenericAdapter<T>() {
    private var selectedPosition: Int = RecyclerView.NO_POSITION
    private var selectedItem: T? = null
    private var scroller: Scroller? = null

    override fun setItems(items: List<T>) {
        super.setItems(items)
        selectedPosition = items.indexOf(selectedItem)
        scroller?.scroll(selectedPosition)
    }

    fun getSelected(): T? {
        return if (selectedPosition == RecyclerView.NO_POSITION || items.isEmpty()) null
        else {
            items[selectedPosition]
        }
    }

    fun setSelected(selectedItem: T?) {
        this.selectedItem = selectedItem
        if (items.isNotEmpty()) {
            with(items.indexOf(this.selectedItem)) {
                selectedPosition = this
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

    override fun customBind(itemView: View, position: Int) {
        when (position) {
            selectedPosition -> itemView.text_view_1.setBackgroundColor(Color.LTGRAY)
            else -> itemView.text_view_1.setBackgroundColor(Color.TRANSPARENT)//TODO: this is a hack, better color handling
        }

        itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                notifyItemChanged(selectedPosition) //Uncheck old selection
                selectedPosition = position
                notifyItemChanged(selectedPosition) //check new selection
            }
        }
    }
}