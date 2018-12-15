package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*
import org.gnvo.climb.tracking.climbtracker.data.room.repository.*

class AddEditViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryAttempt: AttemptRepository = AttemptRepository(application)
    private val repositoryLocation: LocationRepository = LocationRepository(application)
    private val repositoryRouteGrade: RouteGradeRepository = RouteGradeRepository(application)

    fun getAllLocations(): LiveData<Map<String, Map<String, Location>>> {
        return repositoryLocation.getAll()
    }
    fun getAllRouteGrades(): LiveData<List<RouteGrade>> {
        return repositoryRouteGrade.getAll()
    }

    fun getAttemptWithGradesById(attemptId: Long): LiveData<AttemptWithGrades> {
        return repositoryAttempt.getByIdWithGrades(attemptId)
    }

    fun getLastAttemptWithGrades(): LiveData<AttemptWithGrades> {
        return repositoryAttempt.getLastAttemptWithGrades()
    }

    fun insertAttempt(attempt: Attempt) {
        repositoryAttempt.insert(attempt)
    }

    fun updateAttempt(attempt: Attempt) {
        repositoryAttempt.update(attempt)
    }

    fun insertLocation(location: Location) {
        repositoryLocation.insert(location)
    }

    fun updateLocation(location: Location) {
        repositoryLocation.update(location)
    }
}
