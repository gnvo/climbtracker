package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import java.time.LocalDate

class AttemptHeader (
    val date: LocalDate
) : AttemptListItem() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttemptHeader

        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }

    override fun areItemsTheSame(other: AttemptListItem?): Boolean {
        return equals(other)
    }
}
