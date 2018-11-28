package org.gnvo.climb.tracking.climbtracker.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptRouteCharacteristic

@Dao
interface AttemptRouteCharacteristicDao {
    @Insert
    fun insert(attemptRouteCharacteristic: List<AttemptRouteCharacteristic>?)
}