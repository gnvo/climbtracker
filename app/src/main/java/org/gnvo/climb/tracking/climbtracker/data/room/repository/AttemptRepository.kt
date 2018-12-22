package org.gnvo.climb.tracking.climbtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import org.gnvo.climb.tracking.climbtracker.data.room.AppDatabase
import org.gnvo.climb.tracking.climbtracker.data.room.dao.AttemptDao
import org.gnvo.climb.tracking.climbtracker.data.room.dao.RouteGradeDao
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*
import org.jetbrains.anko.doAsync
import org.threeten.bp.format.DateTimeFormatter

class AttemptRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val attemptDao: AttemptDao? = db?.attemptDao()
    private val routeGradeDao: RouteGradeDao? = db?.routeGradeDao()
    private val allAttemptsWithLocation: LiveData<List<AttemptWithLocation>> = attemptDao?.getAllWithLocation()!!
    private val allAttemptsWithLocationGradesAndHeaders: LiveData<List<AttemptListItem>> =
        Transformations.map(allAttemptsWithLocation, ::addHeadersAndGrades)

    private var formatterDate = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy")

    fun getAllWithLocation(): LiveData<List<AttemptWithLocation>> {
        return allAttemptsWithLocation
    }

    fun getAllWithLocationGradesAndHeaders(): LiveData<List<AttemptListItem>> {
        return allAttemptsWithLocationGradesAndHeaders
    }

    fun getByIdWithLocation(attemptId: Long): LiveData<AttemptWithLocation> {
        return attemptDao?.getByIdWithLocation(attemptId)!!
    }

    fun getLastAttemptWithLocation(): LiveData<AttemptWithLocation> {
        return attemptDao?.getLastWithLocation()!!
    }

    fun update(attempt: Attempt) {
        doAsync {
            attemptDao?.update(attempt)
        }
    }

    fun insert(attempt: Attempt) {
        doAsync {
            attemptDao?.insert(attempt)
        }
    }

    fun delete(attemptId: Long) {
        doAsync {
            attemptDao?.delete(attemptId)
        }
    }

    private fun addHeadersAndGrades(attemptsWithLocations: List<AttemptWithLocation>): List<AttemptListItem> {
        val list: MutableList<AttemptListItem> = mutableListOf()

        var currentDate: String? = null
        for (attemptWithLocation in attemptsWithLocations) {
            val zonedDateTime =
                attemptWithLocation.attempt.instantAndZoneId.instant.atZone(attemptWithLocation.attempt.instantAndZoneId.zoneId)
            val attemptDate = zonedDateTime.format(formatterDate)
            if (attemptDate != currentDate)
                list.add(AttemptHeader(date = attemptDate))
            val attemptWithLocationAndGrades = AttemptWithLocationAndGrades(
                attempt = attemptWithLocation.attempt,
                location = attemptWithLocation.location,
                routeGrade = routeGradeDao?.get(attemptWithLocation.attempt.routeGrade)!!
            )
            list.add(attemptWithLocationAndGrades)
            currentDate = attemptDate
        }

        return list
    }
}