package tracking.climbing.gnvo.org.climbingtracker.data.room.dao

import android.arch.persistence.room.*
import tracking.climbing.gnvo.org.climbingtracker.data.room.pojo.ClimbingStyle

@Dao
interface ClimbingStyleDao {
    @Query("SELECT * FROM climbing_style ORDER BY climbing_style_name ASC")
    fun getAll(): List<ClimbingStyle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(boulderGrades: List<ClimbingStyle>)
}