package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.*

data class PitchWithGrades(
    @ColumnInfo(name = "pitch_number") var pitchNumber: Int? = null,
    @ColumnInfo(name = "attempt_outcome") var attemptOutcome: String? = null,
    @ColumnInfo(name = "climbing_style") var climbingStyle: String? = null,
    @ColumnInfo(name = "route_style") var routeStyle: List<String>? = null,
    @ColumnInfo(name = "length") var length: Int? = null,

    var french: String? = null,
    var yds: String? = null,
    var uiaa: String? = null
)