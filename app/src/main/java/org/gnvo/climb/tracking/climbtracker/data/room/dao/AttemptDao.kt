package org.gnvo.climb.tracking.climbtracker.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Attempt
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithLocation

@Dao
interface AttemptDao {
    @Query(
        "SELECT * " +
                "FROM attempt " +
                "LEFT JOIN location on location.location_id = attempt.location " +
                "ORDER BY attempt.instant DESC"
    )
    fun getAllWithLocation(): LiveData<List<AttemptWithLocation>>

    @Query(
        "SELECT attempt.*, location.* " +
                "FROM attempt " +
                "LEFT JOIN location on location.location_id = attempt.location " +
                "WHERE attempt.id = :attemptId "
    )
    fun getByIdWithLocation(attemptId: Long): LiveData<AttemptWithLocation>

    @Query(
        "SELECT attempt.*, location.* " +
                "FROM attempt " +
                "LEFT JOIN location on location.location_id = attempt.location " +
                "WHERE attempt.id = (SELECT max(id) FROM attempt)"
    )
    fun getLastWithLocation(): LiveData<AttemptWithLocation>

    @Insert
    fun insert(attempt: Attempt?)

    @Update
    fun update(attempt: Attempt)

    @Query("DELETE FROM attempt WHERE id = :attemptId")
    fun delete(attemptId: Long)
}
