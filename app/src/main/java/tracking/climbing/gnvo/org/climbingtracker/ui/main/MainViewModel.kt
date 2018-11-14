package tracking.climbing.gnvo.org.climbingtracker.ui.main

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.AndroidViewModel
import tracking.climbing.gnvo.org.climbingtracker.data.repository.ClimbEntryRepository
import tracking.climbing.gnvo.org.climbingtracker.data.repository.RouteTypeRepository
import tracking.climbing.gnvo.org.climbingtracker.data.room.ClimbEntry
import tracking.climbing.gnvo.org.climbingtracker.data.room.RouteType
import java.util.concurrent.Executors

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repositoryClimbEntry: ClimbEntryRepository = ClimbEntryRepository(application, Executors.newSingleThreadScheduledExecutor())
    private val repositoryRouteType: RouteTypeRepository = RouteTypeRepository(application, Executors.newSingleThreadScheduledExecutor())
    private val climbingEntries: LiveData<List<ClimbEntry>> = repositoryClimbEntry.getAllClimbingEntries()

    fun insertClimbEntry(climbEntry: ClimbEntry){
        repositoryClimbEntry.insert(climbEntry)
    }

    fun updateClimbEntry(climbEntry: ClimbEntry){
        repositoryClimbEntry.update(climbEntry)
    }

    fun deleteClimbEntry(climbEntry: ClimbEntry){
        repositoryClimbEntry.delete(climbEntry)
    }

    fun deleteAllClimbingEntries(){
        repositoryClimbEntry.deleteAllClimbingEntries()
    }

    fun getAllClimbingEntries(): LiveData<List<ClimbEntry>>{
        return climbingEntries
    }

    fun getAllRouteTypeNames(): LiveData<List<String>>{
        return repositoryRouteType.getAllRouteTypeNames()
    }

    fun insertRouteType(routeType: RouteType) {
        return repositoryRouteType.insert(routeType)
    }
}
