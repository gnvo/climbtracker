package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import java.util.*

data class RouteGrade(
    var french: String? = null,
    var yds: String? = null,
    var uiaa: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RouteGrade

        if (french != other.french) return false
        if (yds != other.yds) return false
        if (uiaa != other.uiaa) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(french, yds, uiaa)
    }
}
