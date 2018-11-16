package tracking.climbing.gnvo.org.climbingtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "route_style")
data class RouteStyle(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String
){
    companion object {
        fun initialData(): List<RouteStyle> {
            return listOf(
                RouteStyle(name = "Slab"),
                RouteStyle(name = "Face climb"),
                RouteStyle(name = "Overhang"),
                RouteStyle(name = "Tufas"),
                RouteStyle(name = "Pockets")
            )
        }
    }
}
