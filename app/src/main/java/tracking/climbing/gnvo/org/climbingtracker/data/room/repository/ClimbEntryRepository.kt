package tracking.climbing.gnvo.org.climbingtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import tracking.climbing.gnvo.org.climbingtracker.data.room.AppDatabase
import tracking.climbing.gnvo.org.climbingtracker.data.room.dao.ClimbEntryDao
import tracking.climbing.gnvo.org.climbingtracker.data.room.pojo.ClimbEntry
import java.util.concurrent.Executor


class ClimbEntryRepository(application: Application, private val executor: Executor) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
//    private val attemptOutcomeDao: AttemptOutcomeDao? = db?.attemptOutcomeDao()
    private val climbEntryDao: ClimbEntryDao? = db?.climbEntryDao()
//    private val climbingStyleDao: ClimbingStyleDao? = db?.climbingStyleDao()
//    private val pitchDao: PitchDao? = db?.pitchDao()
//    private val routeGradeDao: RouteGradeDao? = db?.routeGradeDao()
//    private val routeStyleDao: RouteStyleDao? = db?.routeStyleDao()
//    private val routeTypeDao: RouteTypeDao? = db?.routeTypeDao()
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