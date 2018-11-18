package org.gnvo.climbing.tracking.climbingtracker.data.room.repository

import android.app.Application
import org.gnvo.climbing.tracking.climbingtracker.data.room.AppDatabase
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.PitchDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.Pitch
import org.jetbrains.anko.doAsync


class PitchRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val pitchDao: PitchDao? = db?.pitchDao()

    fun insert(pitches: List<Pitch?>) {
        doAsync {
            pitchDao?.insert(pitches)
        }
    }

    fun deleteClimbEntryById(climbEntryId: Long?) {
        doAsync {
            pitchDao?.deleteByClimbEntryId(climbEntryId)
        }
    }
}