package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface ClimbEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(climbEntry: ClimbEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(climbEntry: List<ClimbEntry>)

    @Update
    fun update(climbEntry: ClimbEntry)

    @Delete
    fun delete(climbEntry: ClimbEntry)

    @Query("DELETE FROM climbing_entry_table")
    fun deleteAllClimbingEntries()

    @Query("SELECT * FROM climbing_entry_table ORDER BY priority DESC")
    fun getAllClimbingEntries(): LiveData<List<ClimbEntry>>
}