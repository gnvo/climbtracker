package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.TypeConverters
import org.gnvo.climbing.tracking.climbingtracker.data.room.Converters
import java.time.LocalDateTime

@TypeConverters(Converters::class)
data class ClimbEntrySummary(
    @ColumnInfo(name = "climb_entry_id") val climbEntryId: Long? = null,
    val datetime: LocalDateTime,
    @ColumnInfo(name = "route_type") val routeType: String? = null,
    val pitches: List<PitchWithGrades>? = null
)
