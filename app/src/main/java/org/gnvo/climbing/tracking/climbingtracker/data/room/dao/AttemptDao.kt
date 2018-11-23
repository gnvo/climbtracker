package org.gnvo.climbing.tracking.climbingtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.Attempt
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.AttemptWithDetails

@Dao
interface AttemptDao {
    @Query(
        "SELECT attempt.*, route_grade.*  " +
                "FROM attempt " +
                "INNER JOIN route_grade on route_grade.route_grade_id = attempt.route_grade " +
                "ORDER BY datetime ASC"
    )
    fun getAllWithDetails(): LiveData<List<AttemptWithDetails>>

    @Query("SELECT attempt.*, route_grade.*  " +
            "FROM attempt " +
            "INNER JOIN route_grade on route_grade.route_grade_id = attempt.route_grade " +
            "WHERE attempt.id = :attemptId " +
            "ORDER BY datetime ASC"
    )
    fun getByIdWithDetails(attemptId:Long): LiveData<AttemptWithDetails>

    @Query("DELETE FROM attempt")
    fun deleteAll()

    @Insert
    fun insert(attempt: Attempt?)

    @Update
    fun update(attempt: Attempt)
//    @Insert
//    fun init(attempt: List<Attempt>)
//
//
//    @Delete
//    fun delete(attempt: ClimbEntry)
//
//    @Query("DELETE FROM climbing_entry WHERE id = :attemptId")
//    fun deleteByClimbEntryId(attemptId: Long?)
//
//
//    @Query("SELECT * FROM climbing_entry ORDER BY datetime ASC")
//    fun getAllClimbingEntries(): LiveData<List<ClimbEntry>>
//
//    @Transaction
//    @Query("SELECT " +
//            "climb_entry_id, " +
//            "datetime, " +
//            "name, " +
//            "area, " +
//            "sector, " +
//            "rating, " +
//            "route_type, " +
//            "comment, " +
//            "group_concat(pitch_number || \"@@\" || french || \"@@\" || uiaa || \"@@\" || yds || \"@@\" || route_grade_id || \"@@\" || pitch.id || \"@@\" || pitch.attempt_outcome || \"@@\" || pitch.climbing_style, \"^^\") as pitches_full " +
//            "FROM climbing_entry " +
//            "INNER JOIN pitch ON pitch.climb_entry_id = climbing_entry.id " +
//            "INNER JOIN route_grade on route_grade.id = pitch.route_grade_id " +
//            "WHERE climbing_entry.id = :attemptId " +
//            "GROUP BY climb_entry_id " +
//            "ORDER BY datetime")
//    fun getFullById(attemptId:Long): LiveData<ClimbEntryFull>
//
//    @Transaction
//    @Query("SELECT " +
//                "climb_entry_id, " +
//                "datetime, " +
//                "name, " +
//                "area, " +
//                "sector, " +
//                "rating, " +
//                "route_type, " +
//                "group_concat(pitch_number || \"@@\" || french || \"@@\" || uiaa || \"@@\" || yds, \"^^\") as pitches " +
//            "FROM climbing_entry " +
//            "INNER JOIN pitch ON pitch.climb_entry_id = climbing_entry.id " +
//            "INNER JOIN route_grade on route_grade.id = pitch.route_grade_id " +
//            "GROUP BY climb_entry_id " +
//            "ORDER BY datetime")
//    fun getAllSummary(): LiveData<List<ClimbEntrySummary>>
}