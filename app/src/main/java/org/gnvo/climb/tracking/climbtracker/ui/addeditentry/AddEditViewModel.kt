package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*
import org.gnvo.climb.tracking.climbtracker.data.room.repository.*

class AddEditViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryAttempt: AttemptRepository = AttemptRepository(application)
    private val repositoryRouteGrade: RouteGradeRepository = RouteGradeRepository(application)

    fun insertAttempt(attempt: Attempt) {
        repositoryAttempt.insert(attempt)
    }

    fun getAllRouteGrades(): LiveData<List<RouteGrade>> {
        return repositoryRouteGrade.getAll()
    }

    fun getAttemptWithGradesById(attemptId: Long): LiveData<AttemptWithGrades> {
        return repositoryAttempt.getByIdWithGrades(attemptId)
    }

    fun updateAttempt(attempt: Attempt) {
        repositoryAttempt.update(attempt)
    }
}
