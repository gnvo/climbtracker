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

    @Delete
    fun delete(attempt: Attempt)
}