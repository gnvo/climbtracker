package org.gnvo.climbing.tracking.climbingtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climbing.tracking.climbingtracker.data.room.AppDatabase
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.ClimbEntryDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.RouteGradeDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.RouteGrade
import java.util.concurrent.Executor


class RouteGradeRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val routeGradeDao: RouteGradeDao? = db?.routeGradeDao()
    private val allRouteGrades: LiveData<List<RouteGrade>> = routeGradeDao?.getAll()!!

    fun getAll(): LiveData<List<RouteGrade>> {
        return allRouteGrades
    }
}