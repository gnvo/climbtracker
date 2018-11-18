package org.gnvo.climbing.tracking.climbingtracker.data.room.dao

import android.arch.persistence.room.*
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.Pitch

@Dao
interface PitchDao {
    @Insert
    fun insert(pitches: List<Pitch?>)

    @Update
    fun update(pitch: Pitch)

    @Query("DELETE FROM pitch WHERE climb_entry_id = :climbEntryId")
    fun deleteByClimbEntryId(climbEntryId: Long?)
}