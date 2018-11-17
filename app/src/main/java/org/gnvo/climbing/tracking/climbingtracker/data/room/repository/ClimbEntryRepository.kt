package org.gnvo.climbing.tracking.climbingtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climbing.tracking.climbingtracker.data.room.AppDatabase
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.ClimbEntryDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.ClimbEntryWithPitchesDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryWithPitches
import org.jetbrains.anko.doAsync


class ClimbEntryRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
//    private val attemptOutcomeDao: AttemptOutcomeDao? = db?.attemptOutcomeDao()
    private val climbEntryDao: ClimbEntryDao? = db?.climbEntryDao()
    private val climbEntryWithPitchesDao: ClimbEntryWithPitchesDao? = db?.climbEntryWithPitchesDao()
//    private val climbingStyleDao: ClimbingStyleDao? = db?.climbingStyleDao()
//    private val pitchDao: PitchDao? = db?.pitchDao()
//    private val routeGradeDao: RouteGradeDao? = db?.routeGradeDao()
//    private val routeStyleDao: RouteStyleDao? = db?.routeStyleDao()
//    private val routeTypeDao: RouteTypeDao? = db?.routeTypeDao()
    private val allClimbingEntries: LiveData<List<ClimbEntry>> = climbEntryDao?.getAllClimbingEntries()!!
    private val allClimbingEntriesWithPitches: LiveData<List<ClimbEntryWithPitches>> = climbEntryWithPitchesDao?.getAll()!!

    fun insert(climbEntry: ClimbEntry?) {
        doAsync {
            climbEntryDao?.insert(climbEntry)
        }
    }

    fun update(climbEntry: ClimbEntry) {
        doAsync {
            climbEntryDao?.update(climbEntry)
        }
    }

    fun delete(climbEntry: ClimbEntry) {
        doAsync {
            climbEntryDao?.delete(climbEntry)
        }
    }

    fun deleteAllClimbingEntries() {
        doAsync {
            climbEntryDao?.deleteAllClimbingEntries()
        }
    }

    fun getAllClimbingEntries(): LiveData<List<ClimbEntry>> {
        return allClimbingEntries
    }

    fun getAllClimbingEntriesWithPitches(): LiveData<List<ClimbEntryWithPitches>> {
        return allClimbingEntriesWithPitches
    }
}