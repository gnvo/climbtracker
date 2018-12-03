package org.gnvo.climb.tracking.climbtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Attempt
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithGrades

@Dao
interface AttemptDao {
    @Query(
        "SELECT * " +
                "FROM attempt " +
                "INNER JOIN route_grade on route_grade.route_grade_id = attempt.route_grade " +
                "ORDER BY datetime DESC"
    )
    fun getAllWithGrades(): LiveData<List<AttemptWithGrades>>

    @Query(
        "SELECT attempt.*, route_grade.* " +
                "FROM attempt " +
                "INNER JOIN route_grade on route_grade.route_grade_id = attempt.route_grade " +
                "WHERE attempt.id = :attemptId "
    )
    fun getByIdWithGrades(attemptId: Long): LiveData<AttemptWithGrades>

    @Query(
        "SELECT attempt.*, route_grade.* " +
                "FROM attempt " +
                "INNER JOIN route_grade on route_grade.route_grade_id = attempt.route_grade " +
                "WHERE attempt.id = (SELECT max(id) FROM attempt)"
    )
    fun getLastWithGrades(): LiveData<AttemptWithGrades>

    @Insert
    fun insert(attempt: Attempt?)

    @Update
    fun update(attempt: Attempt)

    @Delete
    fun delete(attempt: Attempt)
}