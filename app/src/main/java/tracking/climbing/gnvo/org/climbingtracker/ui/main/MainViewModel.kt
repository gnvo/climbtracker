package tracking.climbing.gnvo.org.climbingtracker.ui.main

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.AndroidViewModel
import tracking.climbing.gnvo.org.climbingtracker.data.repository.ClimbEntryRepository
import tracking.climbing.gnvo.org.climbingtracker.data.room.ClimbEntry
import java.util.concurrent.Executors

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ClimbEntryRepository = ClimbEntryRepository(application, Executors.newSingleThreadScheduledExecutor())
    private val climbingEntries: LiveData<List<ClimbEntry>> = repository.getAllClimbingEntries()

    fun insert(climbEntry: ClimbEntry){
        repository.insert(climbEntry)
    }

    fun update(climbEntry: ClimbEntry){
        repository.update(climbEntry)
    }

    fun delete(climbEntry: ClimbEntry){
        repository.delete(climbEntry)
    }

    fun deleteAllClimbingEntries(){
        repository.deleteAllClimbingEntries()
    }

    fun getAllClimbingEntries(): LiveData<List<ClimbEntry>>{
        return climbingEntries
    }
}
