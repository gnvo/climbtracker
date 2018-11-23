package org.gnvo.climbing.tracking.climbingtracker.ui.addeditentry

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.Attempt
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.AttemptWithDetails
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.RouteGrade
import org.gnvo.climbing.tracking.climbingtracker.data.room.repository.AttemptRepository
import org.gnvo.climbing.tracking.climbingtracker.data.room.repository.RouteGradeRepository

class AddEditViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryAttempt: AttemptRepository = AttemptRepository(application)
    private val repositoryRouteGrade: RouteGradeRepository = RouteGradeRepository(application)

    fun insertAttempt(attempt: Attempt) {
        repositoryAttempt.insert(attempt)
    }

    fun getAllRouteGrades(): LiveData<List<RouteGrade>> {
        return repositoryRouteGrade.getAll()
    }

    fun getClimbingEntryFullById(attemptId: Long): LiveData<AttemptWithDetails> {
        return repositoryAttempt.getByIdWithDetails(attemptId)
    }

    fun updateAttempt(attempt: Attempt) {
        repositoryAttempt.update(attempt)
    }
}
