package org.gnvo.climb.tracking.climbtracker.data.room

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.annotation.NonNull
import org.gnvo.climb.tracking.climbtracker.data.room.dao.*
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*
import org.jetbrains.anko.doAsync

@Database(entities = [Attempt::class, RouteGrade::class, Location::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun attemptDao(): AttemptDao
    abstract fun routeGradeDao(): RouteGradeDao
    abstract fun locationDao(): LocationDao

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
            val MIGRATION_2_3 = object : Migration(2, 3) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("CREATE INDEX `index_attempt_location` ON `attempt` (`location`)")
                }
            }

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "database.sql"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        doAsync {
                            INSTANCE?.routeGradeDao()?.init(RouteGrade.initialData())
                        }
                    }
                })
                .addMigrations(MIGRATION_2_3)
                .build()
        }
    }
}