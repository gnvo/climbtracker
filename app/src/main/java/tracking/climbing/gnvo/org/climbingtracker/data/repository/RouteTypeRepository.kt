package tracking.climbing.gnvo.org.climbingtracker.data.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import tracking.climbing.gnvo.org.climbingtracker.data.room.*
import java.util.concurrent.Executor


class RouteTypeRepository(application: Application, private val executor: Executor) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
//    private val attemptOutcomeDao: AttemptOutcomeDao? = db?.attemptOutcomeDao()
//    private val climbEntryDao: ClimbEntryDao? = db?.climbEntryDao()
//    private val climbingStyleDao: ClimbingStyleDao? = db?.climbingStyleDao()
//    private val pitchDao: PitchDao? = db?.pitchDao()
//    private val routeGradeDao: RouteGradeDao? = db?.routeGradeDao()
//    private val routeStyleDao: RouteStyleDao? = db?.routeStyleDao()
    private val routeTypeDao: RouteTypeDao? = db?.routeTypeDao()
    private val allRouteTypes: LiveData<List<RouteType>> = routeTypeDao?.getAll()!!

    fun getAllRouteTypes(): LiveData<List<RouteType>> {
        return allRouteTypes
    }
}