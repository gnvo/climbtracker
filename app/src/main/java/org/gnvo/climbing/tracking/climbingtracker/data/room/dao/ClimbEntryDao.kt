package org.gnvo.climbing.tracking.climbingtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntrySummary

@Dao
interface ClimbEntryDao {
    @Insert
    fun insert(climbEntry: ClimbEntry?): Long

    @Insert
    fun init(climbEntry: List<ClimbEntry>)

    @Update
    fun update(climbEntry: ClimbEntry)

    @Delete
    fun delete(climbEntry: ClimbEntry)

    @Query("DELETE FROM climbing_entry WHERE id = :climbEntryId")
    fun deleteByClimbEntryId(climbEntryId: Long?)

    @Query("DELETE FROM climbing_entry")
    fun deleteAllClimbingEntries()

    @Query("SELECT * FROM climbing_entry ORDER BY datetime ASC")
    fun getAllClimbingEntries(): LiveData<List<ClimbEntry>>

    @Transaction
    @Query("SELECT " +
            "climb_entry_id, datetime, route_type, group_concat(pitch_number || \"/\" || french || \"/\" || uiaa || \"/\" || yds) as pitches " +
            "FROM climbing_entry " +
            "INNER JOIN pitch ON pitch.climb_entry_id = climbing_entry.id " +
            "INNER JOIN route_grade on route_grade.id = pitch.route_grade_id " +
            "GROUP BY climb_entry_id " +
            "ORDER BY datetime")
    fun getAllSummary(): LiveData<List<ClimbEntrySummary>>

}