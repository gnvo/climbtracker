package tracking.climbing.gnvo.org.climbingtracker.data.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "climbing_style")
data class ClimbingStyle(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "climbing_style_id") val climbingStyleId: Int? = null,
    @ColumnInfo(name = "climbing_style_name") val climbingStyleName: String
){
    companion object {
        fun initialData(): List<ClimbingStyle> {
            return listOf(
                ClimbingStyle(climbingStyleName="Lead"),
                ClimbingStyle(climbingStyleName="Follow"),
                ClimbingStyle(climbingStyleName="Top rope"),
                ClimbingStyle(climbingStyleName="Solo")
            )
        }
    }
}
