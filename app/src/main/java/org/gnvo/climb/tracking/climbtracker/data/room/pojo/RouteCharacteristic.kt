package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "route_characteristic")
data class RouteCharacteristic(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "route_characteristic_id") var routeCharacteristicId: Long? = null,
    @ColumnInfo(name = "route_characteristic_name") var routeCharacteristicName: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RouteCharacteristic

        if (routeCharacteristicName != other.routeCharacteristicName) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(routeCharacteristicId, routeCharacteristicName)
    }

    override fun toString(): String {
        return routeCharacteristicName!!
    }

    companion object {
        fun initialData(): List<RouteCharacteristic> {
            return listOf(
                RouteCharacteristic(routeCharacteristicName = "Tufa"),
                RouteCharacteristic(routeCharacteristicName = "Pockets"),
                RouteCharacteristic(routeCharacteristicName = "Overhang"),
                RouteCharacteristic(routeCharacteristicName = "Crimp"),
                RouteCharacteristic(routeCharacteristicName = "Slopers"),
                RouteCharacteristic(routeCharacteristicName = "Slab")
            )
        }
    }
}
