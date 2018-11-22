package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.*

data class AttemptWithDetails(
    @Embedded val attempt: Attempt,
    @Embedded val routeGrade: RouteGrade
) {
    override fun equals(other: Any?): Boolean {
        return attempt.equals(other) &&
                routeGrade.equals(other)
    }
}