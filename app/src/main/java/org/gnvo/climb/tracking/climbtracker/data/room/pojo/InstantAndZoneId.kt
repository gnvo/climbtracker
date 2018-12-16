package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

data class InstantAndZoneId (
    var instant: Instant,
    @ColumnInfo(name = "zone_id") var zoneId: ZoneId
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