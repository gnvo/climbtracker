package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

data class InstantAndZoneId (
    var instant: Instant,
    @ColumnInfo(name = "zone_id") var zoneId: ZoneId
)