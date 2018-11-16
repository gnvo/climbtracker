package tracking.climbing.gnvo.org.climbingtracker.data.room.pojo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "climbing_style")
data class ClimbingStyle(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String
){
    companion object {
        fun initialData(): List<ClimbingStyle> {
            return listOf(
                ClimbingStyle(name = "Lead"),
                ClimbingStyle(name = "Follow"),
                ClimbingStyle(name = "Top rope"),
                ClimbingStyle(name = "Solo")
            )
        }
    }
}
