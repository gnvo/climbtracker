package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.support.annotation.NonNull
import java.util.concurrent.Executors

@Database(entities = [ClimbEntry::class], version = 1)
//@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun climbEntryDao(): ClimbEntryDao

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
                "my-database"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadScheduledExecutor()
                            .execute {
                                INSTANCE?.climbEntryDao()?.insert(ClimbEntry.initialData())
                            }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}