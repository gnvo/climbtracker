package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.*

data class AttemptWithDetails(
    @Embedded val attempt: Attempt,
    @Embedded val climbStyle: ClimbStyle,
    @Embedded val outcome: Outcome,
    @Embedded val routeGrade: RouteGrade,
    @Embedded val routeType: RouteType,
    @ColumnInfo(name = "route_characteristics") val routeCharacteristics: List<RouteCharacteristic>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttemptWithDetails

        if (attempt != other.attempt) return false
        if (climbStyle != other.climbStyle) return false
        if (outcome != other.outcome) return false
        if (routeGrade != other.routeGrade) return false
        if (routeType != other.routeType) return false
        if (routeCharacteristics != other.routeCharacteristics) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attempt.hashCode()
        result = 31 * result + climbStyle.hashCode()
        result = 31 * result + outcome.hashCode()
        result = 31 * result + routeGrade.hashCode()
        result = 31 * result + routeType.hashCode()
        result = 31 * result + (routeCharacteristics?.hashCode() ?: 0)
        return result
    }
}