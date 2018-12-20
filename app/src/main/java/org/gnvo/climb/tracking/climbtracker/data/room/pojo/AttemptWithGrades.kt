package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.Embedded

data class AttemptWithGrades(
    @Embedded val attempt: Attempt,
    @Embedded var routeGrade: RouteGrade? = null,
    @Embedded val location: Location? = null
) : AttemptListItem() {
    override fun itemsAreEqualOrHaveSameId(other: AttemptListItem?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttemptWithGrades

        if (attempt.id != other.attempt.id) return false

        return true
    }
}