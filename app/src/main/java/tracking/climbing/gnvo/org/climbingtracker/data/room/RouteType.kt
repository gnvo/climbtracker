package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "route_type")
data class RouteType(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "name") val name: String
){
    companion object {
        fun initialData(): List<RouteType> {
            return listOf(
                RouteType(name="Sport"),
                RouteType(name="Trad"),
                RouteType(name="Boulder")
            )
        }
    }
}
