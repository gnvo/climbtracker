package tracking.climbing.gnvo.org.climbingtracker.data.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import tracking.climbing.gnvo.org.climbingtracker.data.room.AppDatabase
import tracking.climbing.gnvo.org.climbingtracker.data.room.ClimbEntry
import tracking.climbing.gnvo.org.climbingtracker.data.room.ClimbEntryDao
import java.util.concurrent.Executor


class ClimbEntryRepository(application: Application, private val executor: Executor) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val climbEntryDao: ClimbEntryDao? = db?.climbEntryDao()
    private val allClimbingEntries: LiveData<List<ClimbEntry>> = climbEntryDao?.getAllClimbingEntries()!!

    fun insert(climbEntry: ClimbEntry) {
        executor.execute {
            climbEntryDao?.insert(climbEntry)
        }
    }

    fun update(climbEntry: ClimbEntry) {
        executor.execute {
            climbEntryDao?.update(climbEntry)
        }
    }

    fun delete(climbEntry: ClimbEntry) {
        executor.execute {
            climbEntryDao?.delete(climbEntry)
        }
    }

    fun deleteAllClimbingEntries() {
        executor.execute {
            climbEntryDao?.deleteAllClimbingEntries()
        }
    }

    fun getAllClimbingEntries(): LiveData<List<ClimbEntry>> {
        return allClimbingEntries
    }
}