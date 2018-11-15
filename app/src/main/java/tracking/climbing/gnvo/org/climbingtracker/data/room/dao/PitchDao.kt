package tracking.climbing.gnvo.org.climbingtracker.data.room.dao

import android.arch.persistence.room.*
import tracking.climbing.gnvo.org.climbingtracker.data.room.pojo.Pitch

@Dao
interface PitchDao {
    @Query("SELECT * FROM pitch ORDER BY pitch_number ASC")
    fun getAll(): List<Pitch>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(pitches: List<Pitch>)
}