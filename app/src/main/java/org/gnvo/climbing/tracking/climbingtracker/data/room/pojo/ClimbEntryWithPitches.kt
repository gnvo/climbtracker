package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.*

data class ClimbEntryWithPitches(
    @Embedded var climbEntry: ClimbEntry? = null,
    @Relation(parentColumn = "id", entityColumn = "climb_entry_id") var pitches: List<Pitch> = ArrayList()
)
