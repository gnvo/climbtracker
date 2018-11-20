package org.gnvo.climbing.tracking.climbingtracker.data.room

import android.arch.persistence.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.PitchWithGrades
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun fromPitches(pitches: String): List<PitchWithGrades> {
        val pitchesStringList = pitches.split(",")

        return pitchesStringList.map{
            val detailsList = it.split("/")
            PitchWithGrades(
                pitchNumber = detailsList[0].toInt(),
                french = detailsList[1],
                uiaa = detailsList[2],
                yds = detailsList[3],
                routeGradeId = detailsList[4].toLong()
            )
        }
    }

    @TypeConverter
    fun fromRouteStyleList(routeStyle: List<String>?): String? {
        if (routeStyle == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(routeStyle, type)
    }

    @TypeConverter
    fun toRouteStyleList(routeStyleString: String?): List<String>? {
        if (routeStyleString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(routeStyleString, type)
    }
}