package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*
import org.gnvo.climb.tracking.climbtracker.data.room.repository.*

class AddEditViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryAttempt: AttemptRepository = AttemptRepository(application)
    private val repositoryClimbStyle: ClimbStyleRepository = ClimbStyleRepository(application)
    private val repositoryOutcome: OutcomeRepository = OutcomeRepository(application)
    private val repositoryRouteGrade: RouteGradeRepository = RouteGradeRepository(application)
    private val repositoryRouteType: RouteTypeRepository = RouteTypeRepository(application)

    fun insertAttempt(attempt: Attempt) {
        repositoryAttempt.insert(attempt)
    }

    fun getAllClimbStyles(): LiveData<List<ClimbStyle>> {
        return repositoryClimbStyle.getAll()
    }

    fun getAllOutcomes(): LiveData<List<Outcome>> {
        return repositoryOutcome.getAll()
    }

    fun getAllRouteGrades(): LiveData<List<RouteGrade>> {
        return repositoryRouteGrade.getAll()
    }
    fun getAllRouteTypes(): LiveData<List<RouteType>> {
        return repositoryRouteType.getAll()
    }

    fun getAttemptWithDetailsById(attemptId: Long): LiveData<AttemptWithDetails> {
        return repositoryAttempt.getByIdWithDetails(attemptId)
    }

    fun updateAttempt(attempt: Attempt) {
        repositoryAttempt.update(attempt)
    }
}
