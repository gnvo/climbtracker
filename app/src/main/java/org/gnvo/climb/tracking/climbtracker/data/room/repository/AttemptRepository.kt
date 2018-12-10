package org.gnvo.climb.tracking.climbtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climb.tracking.climbtracker.data.room.AppDatabase
import org.gnvo.climb.tracking.climbtracker.data.room.dao.AttemptDao
import org.gnvo.climb.tracking.climbtracker.data.room.dao.LocationDao
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Attempt
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithGrades
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Location
import org.jetbrains.anko.doAsync

class AttemptRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val attemptDao: AttemptDao? = db?.attemptDao()
    private val locationDao: LocationDao? = db?.locationDao()
    private val attemptsWithGrades: LiveData<List<AttemptWithGrades>> = attemptDao?.getAllWithGrades()!!

    fun getAllWithGrades(): LiveData<List<AttemptWithGrades>> {
        return attemptsWithGrades
    }

    fun insertAttemptAndLocation(attempt: Attempt, location: Location?) {
        doAsync {
            if (location?.locationId == null){
                location?.locationId = locationDao?.insert(location!!)
            }
            attempt.location = location?.locationId
            attemptDao?.insert(attempt)
        }
    }

    fun getByIdWithGrades(attemptId: Long): LiveData<AttemptWithGrades> {
        return attemptDao?.getByIdWithGrades(attemptId)!!
    }

    fun getLastAttemptWithGrades(): LiveData<AttemptWithGrades> {
        return attemptDao?.getLastWithGrades()!!
    }

    fun updateAttemptAndLocation(attempt: Attempt, location: Location?) {
        doAsync {
            if (location?.locationId == null){
                location?.locationId = locationDao?.insert(location!!)
            }
            attempt.location = location?.locationId
            attemptDao?.update(attempt)
        }
    }

    fun delete(attempt: Attempt) {
        doAsync {
            attemptDao?.delete(attempt)
        }
    }

    fun getLocations(): LiveData<List<Location>> {
        return locationDao?.getAll()!!
    }
}