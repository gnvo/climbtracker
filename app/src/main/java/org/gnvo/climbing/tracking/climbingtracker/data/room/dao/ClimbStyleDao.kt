package org.gnvo.climbing.tracking.climbingtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbStyle

@Dao
interface ClimbStyleDao {
    @Query("SELECT * FROM climb_style ORDER BY climb_style_id ASC")
    fun getAll(): LiveData<List<ClimbStyle>>

    @Insert
    fun init(climbStyles: List<ClimbStyle>)
}