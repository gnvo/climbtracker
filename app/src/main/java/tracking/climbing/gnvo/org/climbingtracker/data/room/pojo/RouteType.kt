package tracking.climbing.gnvo.org.climbingtracker.data.room.pojo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "route_type")
data class RouteType(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String
){
    companion object {
        fun initialData(): List<RouteType> {
            return listOf(
                RouteType(name = "Sport"),
                RouteType(name = "Trad"),
                RouteType(name = "Boulder")
            )
        }
    }
}
