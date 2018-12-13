package org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.recycler_view_item_1.view.*

class GenericAdapterMultipleSelection<T> : GenericAdapter<T>() {

    private var selectedPositions: MutableList<Long> = mutableListOf()
    private var selectedItems: MutableList<T> = mutableListOf()

    override fun setItems(items: ArrayList<T>) {
        super.setItems(items)
        selectedPositions = selectedItems.map {
            items.indexOf(it).toLong()
        }.toMutableList()
    }

    fun getSelected(): List<T>? {
        return if (selectedPositions.isEmpty()) null
        else {
            selectedPositions.map {
                items[it.toInt()]
            }
        }
    }

    fun setSelected(selectedItems: List<T>) {
        this.selectedItems = selectedItems.toMutableList()
        if (items.isNotEmpty()) {
            selectedPositions = selectedItems.map {
                items.indexOf(it).toLong()
            }.toMutableList()
            selectedPositions.forEach {
                notifyItemChanged(it.toInt())
            }
        }
    }

    override fun customBind(itemView: View, position: Int) {
        val isSelected = selectedPositions.contains(position.toLong())
        if (isSelected) {
            itemView.text_view_1.setBackgroundColor(Color.LTGRAY)
        } else {
            itemView.text_view_1.setBackgroundColor(Color.TRANSPARENT)
        }

        itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                notifyItemChanged(position)
                if (isSelected) {
                    selectedPositions.remove(position.toLong())
                } else {
                    selectedPositions.add(position.toLong())
                }
            }
        }
    }
}