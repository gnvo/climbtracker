package org.gnvo.climb.tracking.climbtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climb.tracking.climbtracker.data.room.AppDatabase
import org.gnvo.climb.tracking.climbtracker.data.room.dao.ClimbStyleDao
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*

class ClimbStyleRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val climbStyleDao: ClimbStyleDao? = db?.climbStyleDao()
    private val allItems: LiveData<List<ClimbStyle>> = climbStyleDao?.getAll()!!

    fun getAll(): LiveData<List<ClimbStyle>> {
        return allItems
    }
}