package tracking.climbing.gnvo.org.climbingtracker.data.room.pojo

import android.arch.persistence.room.*

@Entity(tableName = "pitch", foreignKeys = [
    ForeignKey(entity = AttemptOutcome::class, parentColumns = ["id"], childColumns = ["attemptOutcomeId"]),
    ForeignKey(entity = ClimbingStyle::class, parentColumns = ["id"], childColumns = ["climbingStyleId"]),
    ForeignKey(entity = RouteGrade::class, parentColumns = ["id"], childColumns = ["routeGradeId"])
])
data class Pitch(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "pitch_number") var pitchNumber: Int? = 1,
    val attemptOutcomeId: Int? = null,
    val climbingStyleId: Int? = null,
    var routeGradeId: Int//,
//    var routeStyleId?
    )