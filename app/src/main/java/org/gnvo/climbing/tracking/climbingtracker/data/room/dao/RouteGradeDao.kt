package org.gnvo.climbing.tracking.climbingtracker.data.room.dao

import android.arch.persistence.room.*
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.RouteGrade

@Dao
interface RouteGradeDao {
    @Query("SELECT * FROM route_grade ORDER BY french ASC")
    fun getAll(): List<RouteGrade>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(routeGrades: List<RouteGrade>)
}