package tracking.climbing.gnvo.org.climbingtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "pitch")
data class Pitch(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pitch_id") var pitchId: Int? = null,
    @ColumnInfo(name = "pitch_number") var pitchNumber: Int? = -1,
    @Embedded val attemptOutcome: AttemptOutcome,
    @Embedded val climbingStyle: ClimbingStyle,
    @Embedded val routeGrade: RouteGrade,
    @Embedded val routeStyle: RouteStyle
    )