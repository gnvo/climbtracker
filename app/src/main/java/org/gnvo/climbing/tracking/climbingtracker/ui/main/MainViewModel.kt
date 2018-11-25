package org.gnvo.climb.tracking.climbtracker.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Attempt
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithDetails
import org.gnvo.climb.tracking.climbtracker.data.room.repository.AttemptRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryAttempt: AttemptRepository = AttemptRepository(application)
    private val attemptsWithDetails: LiveData<List<AttemptWithDetails>> = repositoryAttempt.getAllWithDetails()

    fun getAllAttemptsWithDetails(): LiveData<List<AttemptWithDetails>> {
        return attemptsWithDetails
    }

    fun deleteAttempt(attempt: Attempt) {
        repositoryAttempt.delete(attempt)
    }
}
