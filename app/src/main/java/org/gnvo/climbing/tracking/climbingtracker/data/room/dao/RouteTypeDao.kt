package org.gnvo.climbing.tracking.climbingtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.RouteType

@Dao
interface RouteTypeDao {
    @Query("SELECT * FROM route_type ORDER BY route_type_id ASC")
    fun getAll(): LiveData<List<RouteType>>

    @Insert
    fun init(routeStyles: List<RouteType>)
}