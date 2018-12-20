package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.Embedded

data class AttemptWithLocationAndGrades(
    @Embedded val attempt: Attempt,
    @Embedded val location: Location? = null,
    @Embedded var routeGrade: RouteGrade
) : AttemptListItem() {
    override fun itemsAreEqualOrHaveSameId(other: AttemptListItem?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttemptWithLocationAndGrades

        if (attempt.id != other.attempt.id) return false

        return true
    }
}