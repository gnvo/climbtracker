package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.AttemptWithDetails
import org.gnvo.climbing.tracking.climbingtracker.data.room.repository.AttemptRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryAttempt: AttemptRepository = AttemptRepository(application)
    private val attemptsWithDetails: LiveData<List<AttemptWithDetails>> = repositoryAttempt.getAllWithDetails()

    fun getAllAttemptsWithDetails(): LiveData<List<AttemptWithDetails>> {
        return attemptsWithDetails
    }

    fun deleteAllAttempts() {
        repositoryAttempt.deleteAll()
    }
}
