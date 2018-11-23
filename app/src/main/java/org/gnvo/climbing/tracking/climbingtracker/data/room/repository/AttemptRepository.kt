package org.gnvo.climbing.tracking.climbingtracker.data.room.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import org.gnvo.climbing.tracking.climbingtracker.data.room.AppDatabase
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.AttemptDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.Attempt
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.AttemptWithDetails
import org.jetbrains.anko.doAsync

class AttemptRepository(application: Application) {
    private val db: AppDatabase? = AppDatabase.getInstance(application = application)
    private val attemptDao: AttemptDao? = db?.attemptDao()
    private val attemptsWithDetails: LiveData<List<AttemptWithDetails>> = attemptDao?.getAllWithDetails()!!

    fun getAllWithDetails(): LiveData<List<AttemptWithDetails>> {
        return attemptsWithDetails
    }

    fun deleteAll() {
        doAsync {
            attemptDao?.deleteAll()
        }
    }

    fun insert(attempt: Attempt) {
        doAsync {
            attemptDao?.insert(attempt)
        }
    }

    fun getByIdWithDetails(attemptId: Long): LiveData<AttemptWithDetails> {
        return attemptDao?.getByIdWithDetails(attemptId)!!
    }

    fun update(attempt: Attempt) {
        doAsync {
            attemptDao?.update(attempt)
        }
    }

//    fun delete(attempt: Attempt) {
//        doAsync {
//            attemptDao?.delete(attempt)
//        }
//    }
//

//
//
//    fun deleteById(attemptId: Long?) {
//        doAsync {
//            attemptDao?.deleteById(attemptId)
//        }
//    }
}