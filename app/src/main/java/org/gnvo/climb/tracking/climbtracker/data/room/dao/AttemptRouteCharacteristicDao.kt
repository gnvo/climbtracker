package org.gnvo.climb.tracking.climbtracker.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptRouteCharacteristic

@Dao
interface AttemptRouteCharacteristicDao {
    @Insert
    fun insert(attemptRouteCharacteristic: List<AttemptRouteCharacteristic>?)

    @Query("DELETE FROM attempt_route_characteristic where attempt = :attemptId")
    fun deleteByAttemptId(attemptId: Long?)
}