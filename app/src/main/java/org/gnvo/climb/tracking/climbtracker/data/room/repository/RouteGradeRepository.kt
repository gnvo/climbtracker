package org.gnvo.climb.tracking.climbtracker.data.room.repository

import android.app.Application
import org.gnvo.climb.tracking.climbtracker.data.room.AppDatabase
import org.gnvo.climb.tracking.climbtracker.data.room.dao.RouteGradeDao
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.RouteGrade

class RouteGradeRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val routeGradeDao: RouteGradeDao? = db?.routeGradeDao()

    private val allItems: HashMap<String, RouteGrade> = routeGradeDao!!.getAll()

    fun getAll(): HashMap<String, RouteGrade> {
        return allItems
    }
}