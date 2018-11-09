package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.*
import tracking.climbing.gnvo.org.climbingtracker.data.room.AttemptOutcome

@Dao
interface AttemptOutcomeDao {
    @Query("SELECT * FROM attempt_outcome ORDER BY attempt_outcome_name ASC")
    fun getAll(): List<AttemptOutcome>

    @Query("SELECT attempt_outcome_name FROM attempt_outcome ORDER BY attempt_outcome_name ASC")
    fun getAllString(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(attemptOutcomes: List<AttemptOutcome>)
}
