package tracking.climbing.gnvo.org.climbingtracker.data.room.pojo

import android.arch.persistence.room.*

//TODO: currently not a table but a JSON in the ClimbEntry POJO, migrate away from JSON
@Entity(tableName = "pitch",
    indices = [Index(value = ["pitch_number"], unique = true)],
    foreignKeys = [ForeignKey(entity = RouteGrade::class, parentColumns = ["id"], childColumns = ["route_grade_id"])
])
data class Pitch(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "pitch_number") var pitchNumber: Int,
    @ColumnInfo(name = "attempt_outcome") val attemptOutcome: String? = null,
    @ColumnInfo(name = "climbing_style") val climbingStyle: String? = null,
    @ColumnInfo(name = "route_grade_id") var routeGradeId: Int,
    @ColumnInfo(name = "route_style") var routeStyle: List<String>? = null
    )