package org.gnvo.climbing.tracking.climbingtracker.data.room

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import android.support.annotation.NonNull
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.ClimbEntryDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.PitchDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.dao.RouteGradeDao
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryWithPitches
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.Pitch
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.RouteGrade
import org.jetbrains.anko.doAsync

@Database(entities = [ClimbEntry::class, Pitch::class, RouteGrade::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pitchDao(): PitchDao
    abstract fun climbEntryDao(): ClimbEntryDao
    abstract fun routeGradeDao(): RouteGradeDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getInstance(application: Application): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = buildDatabase(application)
                }
            }
            return INSTANCE
        }

//        fun destroyDataBase() {
//            INSTANCE = null
//        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "my-database.sql"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        doAsync {
                            INSTANCE?.routeGradeDao()?.init(RouteGrade.initialData())
                            INSTANCE?.climbEntryDao()?.init(ClimbEntry.initialData())
                            INSTANCE?.pitchDao()?.insert(Pitch.initialData())
                        }
                    }
                })
                .build()
        }
    }
}