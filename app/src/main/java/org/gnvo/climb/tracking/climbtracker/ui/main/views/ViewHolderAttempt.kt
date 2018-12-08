package org.gnvo.climb.tracking.climbtracker.ui.main.views

import android.view.View
import kotlinx.android.synthetic.main.attempt_item.view.*
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptListItem
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithGrades
import org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter.EntryAdapter
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class ViewHolderAttempt(itemView: View) : ViewHolder(itemView) {
    private var formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss VV")


    override fun bind(item: AttemptListItem, listener: EntryAdapter.OnItemClickListener?) {
        if (item is AttemptWithGrades) {
            itemView.text_view_climb_style.text = item.attempt.climbStyle
            itemView.text_view_outcome.text = item.attempt.outcome
            itemView.text_view_route_grade.text = item.routeGrade.french

            val attempt = item.attempt
            val listOfDetails = LinkedList<String>()

            val zonedDateTime = attempt.instantAndZoneId.instant.atZone(attempt.instantAndZoneId.zoneId)
            listOfDetails.add(zonedDateTime.format(formatterTime))
            listOfDetails.add(item.attempt.routeType)

            attempt.routeName?.let { listOfDetails.add(it) }
            attempt.length?.let { listOfDetails.add(it.toString() + "mts") }
            item.location?.area?.let { listOfDetails.add(it) }
            item.location?.sector?.let { listOfDetails.add(it) }
            item.attempt.routeCharacteristics?.let { listOfDetails.add(it.joinToString("/")) }
            attempt.rating?.let { listOfDetails.add("rating:" + it.toString() + "/5") }

            itemView.text_view_details.text = listOfDetails.joinToString()

            itemView.setOnClickListener {
                listener?.onItemClick(item)
            }
        }
    }
}