package org.gnvo.climbing.tracking.climbingtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climbing.tracking.climbingtracker.data.room.AppDatabase
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.ClimbEntryDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.PitchDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.RouteGradeDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.*
import org.jetbrains.anko.doAsync

class RouteGradeRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val routeGradeDao: RouteGradeDao? = db?.routeGradeDao()
    private val allItems: LiveData<List<RouteGrade>> = routeGradeDao?.getAll()!!

    fun getAll(): LiveData<List<RouteGrade>> {
        return allItems
    }
}