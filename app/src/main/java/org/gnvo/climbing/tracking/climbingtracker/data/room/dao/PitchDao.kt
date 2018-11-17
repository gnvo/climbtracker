package org.gnvo.climbing.tracking.climbingtracker.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.Pitch


@Dao
interface PitchDao {
    @Insert
    fun insert(pitches: List<Pitch?>)

    @Update
    fun update(pitch: Pitch)
}