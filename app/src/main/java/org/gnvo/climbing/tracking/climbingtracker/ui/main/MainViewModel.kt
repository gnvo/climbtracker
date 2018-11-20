package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.AndroidViewModel
import org.gnvo.climbing.tracking.climbingtracker.data.room.repository.ClimbEntryRepository
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryFull
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryWithPitches
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.RouteGrade
import org.gnvo.climbing.tracking.climbingtracker.data.room.repository.PitchRepository
import org.gnvo.climbing.tracking.climbingtracker.data.room.repository.RouteGradeRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryClimbEntry: ClimbEntryRepository = ClimbEntryRepository(application)
    private val repositoryRouteGrade: RouteGradeRepository = RouteGradeRepository(application)
    private val repositoryPitch: PitchRepository = PitchRepository(application)

    private val climbingEntriesFull: LiveData<List<ClimbEntryFull>> = repositoryClimbEntry.getAllFull()
    private val routeGrades: LiveData<List<RouteGrade>> = repositoryRouteGrade.getAll()

    fun insertClimbEntry(climbEntryWithPitches: ClimbEntryWithPitches) {
        repositoryClimbEntry.insert(climbEntryWithPitches)
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

    fun getAllRouteGrades(): LiveData<List<RouteGrade>> {
        return routeGrades
    }

    fun getAllClimbingEntriesFull(): LiveData<List<ClimbEntryFull>> {
        return climbingEntriesFull
    }

    fun deleteClimbEntryById(climbEntryId: Long?) {
        repositoryPitch.deleteClimbEntryById(climbEntryId)
        repositoryClimbEntry.deleteClimbEntryById(climbEntryId)
    }
}
