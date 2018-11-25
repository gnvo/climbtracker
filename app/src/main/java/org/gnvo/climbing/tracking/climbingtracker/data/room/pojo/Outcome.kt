package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "outcome")
data class Outcome(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "outcome_id") var outcomeId: Long? = null,
    @ColumnInfo(name = "outcome_name") var outcomeName: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Outcome

        if (outcomeName != other.outcomeName) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(outcomeId, outcomeName)
    }

    override fun toString(): String {
        return outcomeName!!
    }

    companion object {
        fun initialData(): List<Outcome> {
            return listOf(
                Outcome(outcomeName = "Onsight"),
                Outcome(outcomeName = "Flash"),
                Outcome(outcomeName = "Redpoint"),
                Outcome(outcomeName = "Fell/Hung")
            )
        }
    }
}
