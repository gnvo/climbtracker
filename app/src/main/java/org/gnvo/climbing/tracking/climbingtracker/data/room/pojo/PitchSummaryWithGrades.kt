package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.*

data class PitchSummaryWithGrades(
    @ColumnInfo(name = "pitch_number") var pitchNumber: Int? = null,
    var french: String? = null,
    var yds: String? = null,
    var uiaa: String? = null
)