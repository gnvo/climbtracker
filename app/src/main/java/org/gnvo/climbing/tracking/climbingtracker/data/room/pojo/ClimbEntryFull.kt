package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.TypeConverters
import org.gnvo.climbing.tracking.climbingtracker.data.room.Converters
import java.time.LocalDateTime

@TypeConverters(Converters::class)
data class ClimbEntryFull(
    @ColumnInfo(name = "climb_entry_id") val climbEntryId: Long? = null,
    val datetime: LocalDateTime,
    var name: String? = null,
    var area: String? = null,
    var sector: String? = null,
    var rating: Int? = null,
    @ColumnInfo(name = "route_type") val routeType: String? = null,
    @ColumnInfo(name = "pitches_full") val pitchesFull: List<PitchFull>? = null
)