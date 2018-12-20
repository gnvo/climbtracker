package org.gnvo.climb.tracking.climbtracker.ui.main.views

import android.view.View
import kotlinx.android.synthetic.main.recycler_view_attempt_item.view.*
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptListItem
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithLocationAndGrades
import org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter.EntryAdapter
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class ViewHolderAttempt(itemView: View, private val alwaysShowGrade: String?) : ViewHolder(itemView) {
    private var formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss VV")

    override fun bind(item: AttemptListItem, listener: EntryAdapter.OnItemClickListener?) {
        if (item is AttemptWithLocationAndGrades) {
            itemView.text_view_climb_style.text = item.attempt.climbStyle
            var tryNumber: String? = null
            item.attempt.tryNumber?.let {
                tryNumber = "($it)"
            }
            itemView.text_view_outcome.text = item.attempt.outcome + (tryNumber ?: "")
            val stringBufferRouteGrade = StringBuffer()
            stringBufferRouteGrade.append(item.attempt.routeGrade)

            if (!alwaysShowGrade.isNullOrEmpty()) {
                val routeGradeField = item.routeGrade::class.java.getDeclaredField(alwaysShowGrade)
                routeGradeField.isAccessible = true
                val alwaysShowActualGrade = routeGradeField.get(item.routeGrade)
                if (alwaysShowActualGrade != item.attempt.routeGrade) {
                    stringBufferRouteGrade.append("/"+alwaysShowActualGrade.toString())
                }
            }
            itemView.text_view_route_grade.text = stringBufferRouteGrade.toString()

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