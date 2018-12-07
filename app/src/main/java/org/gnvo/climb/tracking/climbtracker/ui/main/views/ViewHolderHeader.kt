package org.gnvo.climb.tracking.climbtracker.ui.main.views

import android.view.View
import kotlinx.android.synthetic.main.attempt_header.view.*
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptHeader
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptListItem
import org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter.EntryAdapter
import org.threeten.bp.format.DateTimeFormatter

class ViewHolderHeader(itemView: View) : ViewHolder(itemView) {
    private var formatter = DateTimeFormatter.ofPattern("EEEE, yyyy/MM/dd")

    override fun bind(item: AttemptListItem, listener: EntryAdapter.OnItemClickListener?) {
        if (item is AttemptHeader) {
            itemView.list_item_section_text.text = item.date.format(formatter)
        }
    }
}