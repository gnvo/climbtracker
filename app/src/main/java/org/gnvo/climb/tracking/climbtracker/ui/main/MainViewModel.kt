package org.gnvo.climb.tracking.climbtracker.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptListItem
import org.gnvo.climb.tracking.climbtracker.data.room.repository.AttemptRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryAttempt: AttemptRepository = AttemptRepository(application)
    private val attemptsWithLocationGradesAndHeaders: LiveData<List<AttemptListItem>> = repositoryAttempt.getAllWithLocationGradesAndHeaders()

    fun getAllAttemptsWithGrades(): LiveData<List<AttemptListItem>> {
        return attemptsWithLocationGradesAndHeaders
    }
}
