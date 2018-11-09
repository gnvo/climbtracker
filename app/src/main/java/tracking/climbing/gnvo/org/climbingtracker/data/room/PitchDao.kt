package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.*

@Dao
interface PitchDao {
    @Query("SELECT * FROM pitch ORDER BY pitch_number ASC")
    fun getAll(): List<Pitch>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(pitches: List<Pitch>)
}