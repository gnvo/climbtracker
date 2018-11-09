package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.*

@Dao
interface RouteStyleDao {
    @Query("SELECT * FROM route_style ORDER BY route_style_name ASC")
    fun getAll(): List<RouteStyle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(routeStyles: List<RouteStyle>)
}