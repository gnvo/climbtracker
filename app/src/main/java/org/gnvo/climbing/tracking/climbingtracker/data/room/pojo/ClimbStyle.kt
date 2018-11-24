package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "climb_style")
data class ClimbStyle(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "climb_style_id") var climbStyleId: Long? = null,
    @ColumnInfo(name = "climb_style_name") var climbStyleName: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClimbStyle

        if (climbStyleName != other.climbStyleName) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(climbStyleId, climbStyleName)
    }

    override fun toString(): String {
        return climbStyleName!!
    }

    companion object {
        fun initialData(): List<ClimbStyle> {
            return listOf(
                ClimbStyle(climbStyleName = "Lead"),
                ClimbStyle(climbStyleName = "Follow"),
                ClimbStyle(climbStyleName = "Top rope"),
                ClimbStyle(climbStyleName = "Solo")
            )
        }
    }
}