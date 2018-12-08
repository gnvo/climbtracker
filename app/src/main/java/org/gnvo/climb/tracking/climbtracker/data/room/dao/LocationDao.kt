package org.gnvo.climb.tracking.climbtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM location ORDER BY area, sector ASC")
    fun getAll(): LiveData<List<Location>>

    @Insert
    fun insert(location: Location): Long

    @Update
    fun update(location: Location)
}