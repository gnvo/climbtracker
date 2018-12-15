package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "location", indices = [Index(value = ["area", "sector"], unique = true)])
data class Location(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "location_id") var locationId: Long? = null,
    var area: String,
    var latitude: Double? = null,
    var longitude: Double? = null,
    @ColumnInfo(name = "rock_type") var rockType: List<String>? = null,
    var sector: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (area != other.area) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (rockType != other.rockType) return false
        if (sector != other.sector) return false

        return true
    }

    override fun hashCode(): Int {
        var result = area.hashCode()
        result = 31 * result + (latitude?.hashCode() ?: 0)
        result = 31 * result + (longitude?.hashCode() ?: 0)
        result = 31 * result + (rockType?.hashCode() ?: 0)
        result = 31 * result + (sector?.hashCode() ?: 0)
        return result
    }
}