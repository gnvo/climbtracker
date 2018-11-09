package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface ClimbEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(climbEntry: ClimbEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(climbEntry: List<ClimbEntry>)

    @Update
    fun update(climbEntry: ClimbEntry)

    @Delete
    fun delete(climbEntry: ClimbEntry)

    @Query("DELETE FROM climbing_entry")
    fun deleteAllClimbingEntries()

//    @Query("SELECT * FROM climbing_entry ORDER BY datetime DESC")
    @Query("SELECT * FROM climbing_entry")
    fun getAllClimbingEntries(): LiveData<List<ClimbEntry>>
}