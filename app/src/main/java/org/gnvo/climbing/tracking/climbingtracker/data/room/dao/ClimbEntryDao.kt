package org.gnvo.climbing.tracking.climbingtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.support.design.R
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry

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

    @Query("SELECT * FROM climbing_entry ORDER BY datetime ASC")
    fun getAllClimbingEntries(): LiveData<List<ClimbEntry>>
}