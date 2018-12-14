package org.gnvo.climb.tracking.climbtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import org.gnvo.climb.tracking.climbtracker.data.room.AppDatabase
import org.gnvo.climb.tracking.climbtracker.data.room.dao.LocationDao
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Location
import org.jetbrains.anko.doAsync

class LocationRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val locationDao: LocationDao? = db?.locationDao()
    private val allItems: LiveData<Map<String, Map<String, Location>>> = Transformations.map(locationDao?.getAll()!!, ::getAllHierarchical)

    fun insert(location: Location) {
        doAsync {
            locationDao?.insert(location)
        }
    }

    fun update(location: Location) {
        doAsync {
            locationDao?.update(location)
        }
    }

    fun getAll(): LiveData<Map<String, Map<String, Location>>> {
        return allItems
    }

    private fun getAllHierarchical(locations: List<Location>): Map<String, Map<String, Location>> {
        var mutableMapAvailableLocations =
                locations?.map { it.area to mutableMapOf<String, Location>() }!!.toMap().toMutableMap()

        locations?.let {
            for (availableLocation in it) {
                val sector = availableLocation.sector ?: ""
                mutableMapAvailableLocations[availableLocation.area]!![sector] = availableLocation
            }
        }

        return mutableMapAvailableLocations
    }
}