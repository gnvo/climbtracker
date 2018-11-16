package tracking.climbing.gnvo.org.climbingtracker.data.room.dao

import android.arch.persistence.room.*
import tracking.climbing.gnvo.org.climbingtracker.data.room.pojo.RouteStyle

@Dao
interface RouteStyleDao {
    @Query("SELECT * FROM route_style ORDER BY name ASC")
    fun getAll(): List<RouteStyle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(routeStyles: List<RouteStyle>)
}