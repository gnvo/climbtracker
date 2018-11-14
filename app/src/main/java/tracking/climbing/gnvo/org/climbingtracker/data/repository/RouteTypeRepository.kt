package tracking.climbing.gnvo.org.climbingtracker.data.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import tracking.climbing.gnvo.org.climbingtracker.data.room.*
import java.util.concurrent.Executor


class RouteTypeRepository(application: Application, private val executor: Executor) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val routeTypeDao: RouteTypeDao? = db?.routeTypeDao()
    private val allRouteTypes: LiveData<List<String>> = routeTypeDao?.getAllString()!!

    fun getAllRouteTypeNames(): LiveData<List<String>> {
        return allRouteTypes
    }

    fun insert(routeType: RouteType) {
        executor.execute {
            routeTypeDao?.insert(routeType)
        }
    }
}