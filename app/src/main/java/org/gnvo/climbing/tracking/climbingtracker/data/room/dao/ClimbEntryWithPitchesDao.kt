package org.gnvo.climbing.tracking.climbingtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryWithPitches

@Dao
interface ClimbEntryWithPitchesDao {
    @Transaction
    @Query("SELECT * FROM climbing_entry ORDER BY datetime ASC")
    fun getAll(): LiveData<List<ClimbEntryWithPitches>>
}