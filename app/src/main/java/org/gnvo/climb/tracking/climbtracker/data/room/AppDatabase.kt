package org.gnvo.climb.tracking.climbtracker.data.room

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import android.support.annotation.NonNull
import org.gnvo.climb.tracking.climbtracker.data.room.dao.*
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*
import org.jetbrains.anko.doAsync

@Database(entities = [Attempt::class, ClimbStyle::class, Outcome::class, RouteGrade::class, RouteType::class, RouteCharacteristic::class, AttemptRouteCharacteristic::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun attemptDao(): AttemptDao
    abstract fun attemptRouteCharacteristicDao(): AttemptRouteCharacteristicDao
    abstract fun routeGradeDao(): RouteGradeDao
    abstract fun climbStyleDao(): ClimbStyleDao
    abstract fun outcomeDao(): OutcomeDao
    abstract fun routeCharacteristicDao(): RouteCharacteristicDao
    abstract fun routeTypeDao(): RouteTypeDao

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

        fun destroyDataBase() {
            INSTANCE = null
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "database.sql"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        doAsync {
                            INSTANCE?.climbStyleDao()?.init(ClimbStyle.initialData())
                            INSTANCE?.outcomeDao()?.init(Outcome.initialData())
                            INSTANCE?.routeGradeDao()?.init(RouteGrade.initialData())
                            INSTANCE?.routeTypeDao()?.init(RouteType.initialData())
                            INSTANCE?.routeCharacteristicDao()?.init(RouteCharacteristic.initialData())
                        }
                    }
                })
                .build()
        }
    }
}