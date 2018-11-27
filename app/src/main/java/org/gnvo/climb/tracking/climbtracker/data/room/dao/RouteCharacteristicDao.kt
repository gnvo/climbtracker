package org.gnvo.climb.tracking.climbtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.RouteCharacteristic

@Dao
interface RouteCharacteristicDao {
    @Query("SELECT * FROM route_characteristic ORDER BY route_characteristic_name ASC")
    fun getAll(): LiveData<List<RouteCharacteristic>>

    @Insert
    fun init(routeCharacteristics: List<RouteCharacteristic>)
}