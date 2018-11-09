package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time//.toLong()
    }
}