package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

data class InstantAndZoneId (
    var instant: Instant,
    var zoneId: ZoneId
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InstantAndZoneId

        if (instant != other.instant) return false
        if (zoneId != other.zoneId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = instant.hashCode()
        result = 31 * result + zoneId.hashCode()
        return result
    }
}