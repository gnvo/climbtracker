package org.gnvo.climb.tracking.climbtracker.data.room

import android.arch.persistence.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.RouteCharacteristic
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
    fun fromRouteCharacteristics(routeCharacteristics: String?): List<RouteCharacteristic>? {
        val routeCharacteristicsStringList = routeCharacteristics?.split("^^")

        return routeCharacteristicsStringList?.map{
            val detailsList = it.split("@@")
            RouteCharacteristic(
                routeCharacteristicId = detailsList[0].toLong(),
                routeCharacteristicName = detailsList[1]
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