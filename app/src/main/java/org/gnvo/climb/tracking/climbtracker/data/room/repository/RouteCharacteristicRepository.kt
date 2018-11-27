package org.gnvo.climb.tracking.climbtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climb.tracking.climbtracker.data.room.AppDatabase
import org.gnvo.climb.tracking.climbtracker.data.room.dao.RouteCharacteristicDao
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*

class RouteCharacteristicRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val routeCharacteristicDao: RouteCharacteristicDao? = db?.routeCharacteristicDao()
    private val allItems: LiveData<List<RouteCharacteristic>> = routeCharacteristicDao?.getAll()!!

    fun getAll(): LiveData<List<RouteCharacteristic>> {
        return allItems
    }
}