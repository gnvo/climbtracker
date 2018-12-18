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

@Database(entities = [Attempt::class, Location::class], version = 5)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun attemptDao(): AttemptDao
    abstract fun locationDao(): LocationDao
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

            val MIGRATION_4_5 = object : Migration(4, 5) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("PRAGMA foreign_keys=off;")
                    database.execSQL("BEGIN TRANSACTION;")
                    database.execSQL("ALTER TABLE attempt RENAME TO attempt_old;")
                    database.execSQL("CREATE TABLE `attempt` " +
                            "(`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "`climb_style` TEXT NOT NULL," +
                            "`outcome` TEXT NOT NULL," +
                            "`route_grade` TEXT NOT NULL," +
                            "`route_type` TEXT NOT NULL," +
                            "`route_name` TEXT," +
                            "`comment` TEXT," +
                            "`rating` INTEGER," +
                            "`route_characteristics` TEXT," +
                            "`length` INTEGER," +
                            "`location` INTEGER," +
                            "`instant` INTEGER NOT NULL," +
                            "`zone_id` TEXT NOT NULL," +
                            "FOREIGN KEY(`location`) REFERENCES `location`(`location_id`) ON UPDATE NO ACTION ON DELETE NO ACTION );")
                    database.execSQL("INSERT INTO attempt ( id,climb_style,outcome,route_grade,route_type,route_name,comment,rating,route_characteristics,length,location,instant,zone_id )" +
                            "SELECT id,climb_style,outcome,route_grade.french,route_type,route_name,comment,rating,route_characteristics,length,location,instant,zone_id " +
                            "FROM attempt_old " +
                            "INNER JOIN route_grade on route_grade.route_grade_id = attempt_old.route_grade ;")
                    database.execSQL("DROP TABLE attempt_old;")
                    database.execSQL("CREATE INDEX `index_attempt_location` ON `attempt` (`location`);")
                    database.execSQL("COMMIT;")
                    database.execSQL("PRAGMA foreign_keys=on;")
                }
            }

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "database.sql"
            )
                .addMigrations(MIGRATION_2_3)
                .addMigrations(MIGRATION_3_4)
                .addMigrations(MIGRATION_4_5)
                .build()
        }
    }
}