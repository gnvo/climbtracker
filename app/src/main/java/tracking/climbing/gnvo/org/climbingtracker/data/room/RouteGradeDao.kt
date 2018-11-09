package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.*

@Dao
interface RouteGradeDao {
    @Query("SELECT * FROM route_grade ORDER BY french ASC")
    fun getAll(): List<RouteGrade>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(routeGrades: List<RouteGrade>)
}