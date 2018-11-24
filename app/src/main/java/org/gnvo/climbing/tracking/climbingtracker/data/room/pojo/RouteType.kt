package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "route_type")
data class RouteType(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "route_type_id") var routeTypeId: Long? = null,
    @ColumnInfo(name = "route_type_name") var routeTypeName: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RouteType

        if (routeTypeName != other.routeTypeName) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(routeTypeId, routeTypeName)
    }

    override fun toString(): String {
        return routeTypeName!!
    }

    companion object {
        fun initialData(): List<RouteType> {
            return listOf(
                RouteType(routeTypeName = "Trad"),
                RouteType(routeTypeName = "Sport"),
                RouteType(routeTypeName = "Indoors")
            )
        }
    }
}
