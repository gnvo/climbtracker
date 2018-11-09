package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "climbing_entry_table")
data class ClimbEntry(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val name: String,
//    val coordinates: String,
//    val site: String,
//    val sector: String,
//    val datetime: java.sql.Date,
    val comments: String,
    val priority: Int? = null
) {
    companion object {
        fun initialData(): List<ClimbEntry> {
            return listOf(
                ClimbEntry(name = "name 1", comments = "comments 1", priority = 1)
            )
        }
    }
}
