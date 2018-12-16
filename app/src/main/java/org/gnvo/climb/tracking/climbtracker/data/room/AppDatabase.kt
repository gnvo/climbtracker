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

@Database(entities = [Attempt::class, RouteGrade::class, Location::class], version = 4)
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

            val MIGRATION_3_4 = object : Migration(3, 4) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("PRAGMA foreign_keys=off;")
                    database.execSQL("BEGIN TRANSACTION;")
                    database.execSQL("ALTER TABLE attempt RENAME TO attempt_old;")
                    database.execSQL("CREATE TABLE `attempt` " +
                            "(`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "`climb_style` TEXT NOT NULL," +
                            "`outcome` TEXT NOT NULL," +
                            "`route_grade` INTEGER NOT NULL," +
                            "`route_type` TEXT NOT NULL," +
                            "`route_name` TEXT," +
                            "`comment` TEXT," +
                            "`rating` INTEGER," +
                            "`route_characteristics` TEXT," +
                            "`length` INTEGER," +
                            "`location` INTEGER," +
                            "`instant` INTEGER NOT NULL," +
                            "`zone_id` TEXT NOT NULL," +
                            "FOREIGN KEY(`route_grade`) REFERENCES `route_grade`(`route_grade_id`) ON UPDATE NO ACTION ON DELETE NO ACTION ," +
                            "FOREIGN KEY(`location`) REFERENCES `location`(`location_id`) ON UPDATE NO ACTION ON DELETE NO ACTION );")
                    database.execSQL("INSERT INTO attempt ( id,climb_style,outcome,route_grade,route_type,route_name,comment,rating,route_characteristics,length,location,instant,zone_id )" +
                            "SELECT  id,climb_style,outcome,route_grade,route_type,route_name,comment,rating,routeCharacteristics,length,location,instant,zoneId " +
                            "FROM attempt_old;")
                    database.execSQL("DROP TABLE attempt_old;\n")
                    database.execSQL("CREATE INDEX `index_attempt_location` ON `attempt` (`location`);")
                    database.execSQL("CREATE INDEX `index_attempt_route_grade` ON `attempt` (`route_grade`);")
                    database.execSQL("COMMIT;")
                    database.execSQL("PRAGMA foreign_keys=on;")
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
                .addMigrations(MIGRATION_3_4)
                .build()
        }
    }
}