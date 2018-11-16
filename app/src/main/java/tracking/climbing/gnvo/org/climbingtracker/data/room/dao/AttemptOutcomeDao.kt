package tracking.climbing.gnvo.org.climbingtracker.data.room.dao

import android.arch.persistence.room.*
import tracking.climbing.gnvo.org.climbingtracker.data.room.pojo.AttemptOutcome

@Dao
interface AttemptOutcomeDao {
    @Query("SELECT * FROM attempt_outcome ORDER BY name ASC")
    fun getAll(): List<AttemptOutcome>

    @Query("SELECT name FROM attempt_outcome ORDER BY name ASC")
    fun getAllString(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(attemptOutcomes: List<AttemptOutcome>)
}
