package tracking.climbing.gnvo.org.climbingtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import tracking.climbing.gnvo.org.climbingtracker.data.room.pojo.RouteType

@Dao
interface RouteTypeDao {
    @Query("SELECT * FROM route_type ORDER BY name ASC")
    fun getAll(): LiveData<List<RouteType>>

    @Query("SELECT name FROM route_type ORDER BY name ASC")
    fun getAllString(): LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(routeTypes: List<RouteType>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(routeType: RouteType)
}