package org.gnvo.climb.tracking.climbtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import org.gnvo.climb.tracking.climbtracker.data.room.AppDatabase
import org.gnvo.climb.tracking.climbtracker.data.room.dao.AttemptDao
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*
import org.jetbrains.anko.doAsync
import org.threeten.bp.format.DateTimeFormatter

class AttemptRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val attemptDao: AttemptDao? = db?.attemptDao()
    private val allAttemptsWithGradesAndHeaders: LiveData<List<AttemptListItem>> =
        Transformations.map(attemptDao?.getAllWithGrades()!!, ::getAllWithDateHeaders)

    private var formatterDate = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy")

    fun getAllWithGradesAndHeaders(): LiveData<List<AttemptListItem>> {
        return allAttemptsWithGradesAndHeaders
    }

    fun getByIdWithGrades(attemptId: Long): LiveData<AttemptWithGrades> {
        return attemptDao?.getByIdWithGrades(attemptId)!!
    }

    fun getLastAttemptWithGrades(): LiveData<AttemptWithGrades> {
        return attemptDao?.getLastWithGrades()!!
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

    private fun getAllWithDateHeaders(attemptsWithGrades: List<AttemptWithGrades>): List<AttemptListItem> {
        val list: MutableList<AttemptListItem> = mutableListOf()

        var currentDate: String? = null
        for (attemptWithGrades in attemptsWithGrades){
            val zonedDateTime = attemptWithGrades.attempt.instantAndZoneId.instant.atZone(attemptWithGrades.attempt.instantAndZoneId.zoneId)
            val attemptDate = zonedDateTime.format(formatterDate)
            if (attemptDate != currentDate)
                list.add(AttemptHeader(date = attemptDate))
            list.add(attemptWithGrades)
            currentDate = attemptDate
        }

        return list
    }
}