package tracking.climbing.gnvo.org.climbingtracker.data.room.pojo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "attempt_outcome")
data class AttemptOutcome(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String
){
    companion object {
        fun initialData(): List<AttemptOutcome> {
            return listOf(
                AttemptOutcome(name="Onsight"),
                AttemptOutcome(name="Flash"),
                AttemptOutcome(name="Redpoint"),
                AttemptOutcome(name="Fell/Hung")
            )
        }
    }
}