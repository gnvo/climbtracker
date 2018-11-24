package org.gnvo.climbing.tracking.climbingtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climbing.tracking.climbingtracker.data.room.AppDatabase
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.RouteTypeDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.*

class RouteTypeRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val routeTypeDao: RouteTypeDao? = db?.routeTypeDao()
    private val allItems: LiveData<List<RouteType>> = routeTypeDao?.getAll()!!

    fun getAll(): LiveData<List<RouteType>> {
        return allItems
    }
}