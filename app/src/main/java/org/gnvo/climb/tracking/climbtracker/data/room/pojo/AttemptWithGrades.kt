package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.Embedded

data class AttemptWithGrades(
    @Embedded val attempt: Attempt,
    @Embedded val routeGrade: RouteGrade
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttemptWithGrades

        if (attempt != other.attempt) return false
        if (routeGrade != other.routeGrade) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attempt.hashCode()
        result = 31 * result + routeGrade.hashCode()
        return result
    }
}