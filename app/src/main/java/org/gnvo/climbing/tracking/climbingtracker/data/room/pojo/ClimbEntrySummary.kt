package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import java.util.*

data class ClimbEntrySummary(
    val climb_entry_id: Long? = null,
    val datetime: Date,
    val pitches: String
)
