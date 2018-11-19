package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.*

@Entity(
    tableName = "pitch",
    indices = [Index(value = ["pitch_number", "climb_entry_id"], unique = true)],
    foreignKeys = [
        ForeignKey(entity = RouteGrade::class, parentColumns = ["id"], childColumns = ["route_grade_id"]),
        ForeignKey(entity = ClimbEntry::class, parentColumns = ["id"], childColumns = ["climb_entry_id"])]
)
data class Pitch(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "pitch_number") var pitchNumber: Int? = null,
    @ColumnInfo(name = "attempt_outcome") var attemptOutcome: String? = null,
    @ColumnInfo(name = "climbing_style") var climbingStyle: String? = null,
    @ColumnInfo(name = "route_grade_id") var routeGradeId: Long,
    @ColumnInfo(name = "climb_entry_id") var climbEntryId: Long? = null,
    @ColumnInfo(name = "route_style") var routeStyle: List<String>? = null//TODO: currently storing as JSON, migrate away from JSON
)