package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.*

@Dao
interface RouteTypeDao {
    @Query("SELECT * FROM route_type ORDER BY route_type_name ASC")
    fun getAll(): List<RouteType>

    @Query("SELECT route_type_name FROM route_type ORDER BY route_type_name ASC")
    fun getAllString(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(routeTypes: List<RouteType>)
}