package org.gnvo.climbing.tracking.climbingtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climbing.tracking.climbingtracker.data.room.AppDatabase
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.ClimbEntryDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.PitchDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntrySummary
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryWithPitches
import org.jetbrains.anko.doAsync

class ClimbEntryRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val climbEntryDao: ClimbEntryDao? = db?.climbEntryDao()
    private val pitchDao: PitchDao? = db?.pitchDao()
    private val allSummary: LiveData<List<ClimbEntrySummary>> = climbEntryDao?.getAllSummary()!!

    fun insert(climbEntryWithPitches: ClimbEntryWithPitches) {
        doAsync {
            val climbEntryId = climbEntryDao?.insert(climbEntryWithPitches.climbEntry)
            if (climbEntryId != -1L) {
                for (pitch in climbEntryWithPitches.pitches) {
                    pitch.climbEntryId = climbEntryId!!
                }
                pitchDao?.insert(climbEntryWithPitches.pitches)
            }
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

    fun getAllSummary(): LiveData<List<ClimbEntrySummary>> {
        return allSummary
    }

    fun deleteClimbEntryById(climbEntryId: Long?) {
        doAsync {
            climbEntryDao?.deleteByClimbEntryId(climbEntryId)
        }
    }
}