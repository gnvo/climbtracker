package org.gnvo.climb.tracking.climbtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Outcome

@Dao
interface OutcomeDao {
    @Query("SELECT * FROM outcome ORDER BY outcome_id ASC")
    fun getAll(): LiveData<List<Outcome>>

    @Insert
    fun init(outcomes: List<Outcome>)
}