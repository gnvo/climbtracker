package org.gnvo.climb.tracking.climbtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Attempt
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithDetails

@Dao
interface AttemptDao {
    @Query(
        "SELECT " +
                    "attempt.*, " +
                    "climb_style.*, " +
                    "outcome.*, " +
                    "route_grade.*, " +
                    "route_type.*, " +
                    "group_concat(route_characteristic || \"@@\" || route_characteristic_name, \"^^\") as route_characteristics " +
                "FROM attempt " +
                "INNER JOIN climb_style on climb_style.climb_style_id = attempt.climb_style " +
                "INNER JOIN outcome on outcome.outcome_id = attempt.outcome " +
                "INNER JOIN route_grade on route_grade.route_grade_id = attempt.route_grade " +
                "INNER JOIN route_type on route_type.route_type_id = attempt.route_type " +
                "LEFT JOIN attempt_route_characteristic on attempt_route_characteristic.attempt = attempt.id " +
                "LEFT JOIN route_characteristic on route_characteristic.route_characteristic_id = attempt_route_characteristic.route_characteristic " +
                "ORDER BY datetime DESC"
    )
    fun getAllWithDetails(): LiveData<List<AttemptWithDetails>>

    @Query("SELECT attempt.*, climb_style.*, outcome.*, route_grade.*, route_type.* " +
            "FROM attempt " +
            "INNER JOIN climb_style on climb_style.climb_style_id = attempt.climb_style " +
            "INNER JOIN outcome on outcome.outcome_id = attempt.outcome " +
            "INNER JOIN route_grade on route_grade.route_grade_id = attempt.route_grade " +
            "INNER JOIN route_type on route_type.route_type_id = attempt.route_type " +
            "WHERE attempt.id = :attemptId "
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