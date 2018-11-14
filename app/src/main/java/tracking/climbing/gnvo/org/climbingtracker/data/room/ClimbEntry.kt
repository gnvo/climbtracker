package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.Date

@Entity(tableName = "climbing_entry")
data class ClimbEntry(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val name: String,
    val coordinates: String,
    val site: String,
    val sector: String,
    val datetime: Date,
//    @Embedded val pitches: List<Pitch>,
    @Embedded(prefix = "route_type_") val routeType: RouteType,
    var rating: Int? = null,
    val comment: String
)
{
    companion object {
        fun initialData(): List<ClimbEntry> {
            return listOf(
                ClimbEntry(
                    name = "route name1",
                    coordinates = "1.0,1.0",
                    site = "site1",
                    sector = "sector1",
                    datetime = Date(0),
//                    pitches = List(0),
                    routeType = RouteType(name="Sport"),
                    rating = 5,
                    comment = "comments1")
            )
        }
    }
}
