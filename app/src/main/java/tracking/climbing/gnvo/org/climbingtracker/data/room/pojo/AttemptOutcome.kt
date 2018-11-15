package tracking.climbing.gnvo.org.climbingtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "attempt_outcome")
data class AttemptOutcome(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "attempt_outcome_id") val attemptOutcomeId: Int? = null,
    @ColumnInfo(name = "attempt_outcome_name") val attemptOutcomeName: String
){
    companion object {
        fun initialData(): List<AttemptOutcome> {
            return listOf(
                AttemptOutcome(attemptOutcomeName="Onsight"),
                AttemptOutcome(attemptOutcomeName="Flash"),
                AttemptOutcome(attemptOutcomeName="Redpoint"),
                AttemptOutcome(attemptOutcomeName="Fell/Hung")
            )
        }
    }
}