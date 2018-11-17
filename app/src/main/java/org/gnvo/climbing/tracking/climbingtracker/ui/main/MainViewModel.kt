package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.AndroidViewModel
import org.gnvo.climbing.tracking.climbingtracker.data.room.repository.ClimbEntryRepository
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry
import java.util.concurrent.Executors

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repositoryClimbEntry: ClimbEntryRepository =
        ClimbEntryRepository(
            application,
            Executors.newSingleThreadScheduledExecutor()
        )
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
}
