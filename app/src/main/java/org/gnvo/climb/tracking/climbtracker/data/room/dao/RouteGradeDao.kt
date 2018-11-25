package org.gnvo.climb.tracking.climbtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.RouteGrade

@Dao
interface RouteGradeDao {
    @Query("SELECT * FROM route_grade ORDER BY french ASC")
    fun getAll(): LiveData<List<RouteGrade>>

    @Insert
    fun init(routeGrades: List<RouteGrade>)
}