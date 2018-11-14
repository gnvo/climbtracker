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
    val datetime: Date?,
//    @Embedded val pitches: List<Pitch>,
    @Embedded(prefix = "route_type_") val routeType: RouteType,
    var rating: Int? = null,
    val comment: String
)
//{
//    companion object {
//        fun initialData(): List<ClimbEntry> {
//            return listOf(
//                ClimbEntry(name = "name1",
//                    coordinates = "coordinates1",
//                    site = "site1",
//                    sector = "sector1",
//                    datetime = "datetime1",
//                    pitches = List(0),
//                    routeType = RouteType(),
//                    rating = 5,
//                    comments = "comments1")
//            )
//        }
//    }
//}
