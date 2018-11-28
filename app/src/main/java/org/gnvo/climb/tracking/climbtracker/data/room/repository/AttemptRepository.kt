package org.gnvo.climb.tracking.climbtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climb.tracking.climbtracker.data.room.AppDatabase
import org.gnvo.climb.tracking.climbtracker.data.room.dao.AttemptDao
import org.gnvo.climb.tracking.climbtracker.data.room.dao.AttemptRouteCharacteristicDao
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Attempt
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptRouteCharacteristic
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithDetails
import org.jetbrains.anko.doAsync

class AttemptRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val attemptDao: AttemptDao? = db?.attemptDao()
    private val attemptRouteCharacteristicDao: AttemptRouteCharacteristicDao? = db?.attemptRouteCharacteristicDao()
    private val attemptsWithDetails: LiveData<List<AttemptWithDetails>> = attemptDao?.getAllWithDetails()!!

    fun getAllWithDetails(): LiveData<List<AttemptWithDetails>> {
        return attemptsWithDetails
    }

    fun insert(attempt: Attempt, routeCharacteristicsId: List<Long>?) {
        doAsync {
            val attemptId = attemptDao?.insert(attempt)
            routeCharacteristicsId?.let{ list ->
                val routeCharacteristics = list.map {
                    AttemptRouteCharacteristic(routeCharacteristic = it, attempt = attemptId!!)
                }
                attemptRouteCharacteristicDao?.insert(routeCharacteristics)
            }
        }
    }

    fun getByIdWithDetails(attemptId: Long): LiveData<AttemptWithDetails> {
        return attemptDao?.getByIdWithDetails(attemptId)!!
    }

    fun update(attempt: Attempt) {
        doAsync {
            attemptDao?.update(attempt)
        }
    }

    fun delete(attempt: Attempt) {
        doAsync {
            attemptRouteCharacteristicDao?.deleteByAttemptId(attempt.id)
            attemptDao?.delete(attempt)
        }
    }
}