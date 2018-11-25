package org.gnvo.climb.tracking.climbtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climb.tracking.climbtracker.data.room.AppDatabase
import org.gnvo.climb.tracking.climbtracker.data.room.dao.OutcomeDao
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*

class OutcomeRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val outcomeDao: OutcomeDao? = db?.outcomeDao()
    private val allItems: LiveData<List<Outcome>> = outcomeDao?.getAll()!!

    fun getAll(): LiveData<List<Outcome>> {
        return allItems
    }
}