package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.AndroidViewModel
import org.gnvo.climbing.tracking.climbingtracker.data.room.repository.ClimbEntryRepository
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryWithPitches
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.RouteGrade
import org.gnvo.climbing.tracking.climbingtracker.data.room.repository.RouteGradeRepository
import java.util.concurrent.Executors

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryClimbEntry: ClimbEntryRepository = ClimbEntryRepository(application)
    private val repositoryRouteGrade: RouteGradeRepository = RouteGradeRepository(application)
    private val climbingEntries: LiveData<List<ClimbEntry>> = repositoryClimbEntry.getAllClimbingEntries()
    private val climbingEntriesWithPitches: LiveData<List<ClimbEntryWithPitches>> = repositoryClimbEntry.getAllClimbingEntriesWithPitches()
    private val routeGrades: LiveData<List<RouteGrade>> = repositoryRouteGrade.getAll()

    fun insertClimbEntry(climbEntry: ClimbEntry) {
        repositoryClimbEntry.insert(climbEntry)
    }

    fun updateClimbEntry(climbEntry: ClimbEntry) {
        repositoryClimbEntry.update(climbEntry)
    }

    fun deleteClimbEntry(climbEntry: ClimbEntry) {
        repositoryClimbEntry.delete(climbEntry)
    }

    fun deleteAllClimbingEntries() {
        repositoryClimbEntry.deleteAllClimbingEntries()
    }

    fun getAllClimbingEntries(): LiveData<List<ClimbEntry>> {
        return climbingEntries
    }

    fun getAllClimbingEntriesWithPitches(): LiveData<List<ClimbEntryWithPitches>> {
        return climbingEntriesWithPitches
    }

    fun getAllRouteGrades(): LiveData<List<RouteGrade>> {
        return routeGrades
    }
}
