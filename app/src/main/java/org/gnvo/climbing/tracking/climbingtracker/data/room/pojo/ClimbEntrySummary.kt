package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.TypeConverters
import org.gnvo.climbing.tracking.climbingtracker.data.room.Converters
import java.util.*

@TypeConverters(Converters::class)
data class ClimbEntrySummary(
    @ColumnInfo(name = "climb_entry_id") val climbEntryId: Long? = null,
    val datetime: Date,
    val pitches: List<PitchSummaryWithGrades>? = null
)
