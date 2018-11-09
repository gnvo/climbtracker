package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "route_style")
data class RouteStyle(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "route_style_id") val routeStyleId: Int? = null,
    @ColumnInfo(name = "route_style_name") val routeStyleName: String
){
    companion object {
        fun initialData(): List<RouteStyle> {
            return listOf(
                RouteStyle(routeStyleName="Slab"),
                RouteStyle(routeStyleName="Face climb"),
                RouteStyle(routeStyleName="Overhang"),
                RouteStyle(routeStyleName="Tufas"),
                RouteStyle(routeStyleName="Pockets")
            )
        }
    }
}
