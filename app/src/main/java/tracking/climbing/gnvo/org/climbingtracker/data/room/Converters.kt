package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.TypeConverter
import java.util.Date
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import tracking.climbing.gnvo.org.climbingtracker.data.room.pojo.Pitch


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time//.toLong()
    }

    @TypeConverter
    fun fromPitchList(pitch: List<Pitch>?): String? {
        if (pitch == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Pitch>>() {}.type
        return gson.toJson(pitch, type)
    }

    @TypeConverter
    fun toPitchList(pitchString: String?): List<Pitch>? {
        if (pitchString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Pitch>>() {}.type
        return gson.fromJson(pitchString, type)
    }
}