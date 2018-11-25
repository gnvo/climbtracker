package org.gnvo.climb.tracking.climbtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climb.tracking.climbtracker.data.room.AppDatabase
import org.gnvo.climb.tracking.climbtracker.data.room.dao.RouteGradeDao
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*

class RouteGradeRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val routeGradeDao: RouteGradeDao? = db?.routeGradeDao()
    private val allItems: LiveData<List<RouteGrade>> = routeGradeDao?.getAll()!!

    fun getAll(): LiveData<List<RouteGrade>> {
        return allItems
    }
}