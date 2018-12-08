package org.gnvo.climb.tracking.climbtracker.data.room

import android.arch.persistence.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

class Converters {
    @TypeConverter
    fun fromTimestamp(timestamp: Long?): Instant? {
        return timestamp?.let { Instant.ofEpochMilli(timestamp) }
    }

    @TypeConverter
    fun instantToTimestamp(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

    @TypeConverter
    fun fromZoneId(zoneId: String?): ZoneId? {
        return zoneId?.let { ZoneId.of(it) }
    }

    @TypeConverter
    fun toZoneId(zoneId: ZoneId?): String? {
        return zoneId?.toString()
    }

    @TypeConverter
    fun fromRouteCharacteristics(routeCharacteristics: List<String>?): String? {
        if (routeCharacteristics == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(routeCharacteristics, type)
    }

    @TypeConverter
    fun toRouteCharacteristics(routeCharacteristicsString: String?): List<String>? {
        if (routeCharacteristicsString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(routeCharacteristicsString, type)
    }
}