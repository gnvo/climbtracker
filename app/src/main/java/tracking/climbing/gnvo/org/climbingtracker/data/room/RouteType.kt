package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "route_type")
data class RouteType(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "route_type_id") val routeTypeId: Int? = null,
    @ColumnInfo(name = "route_type_name") val routeTypeName: String
){
    companion object {
        fun initialData(): List<RouteType> {
            return listOf(
                RouteType(routeTypeName="Sport"),
                RouteType(routeTypeName="Trad"),
                RouteType(routeTypeName="Boulder")
            )
        }
    }
}
