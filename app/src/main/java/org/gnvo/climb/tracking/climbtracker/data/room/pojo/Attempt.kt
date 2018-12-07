package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.*
import java.time.LocalDateTime

@Entity(
    tableName = "attempt",
    foreignKeys = [
        ForeignKey(entity = RouteGrade::class, parentColumns = ["route_grade_id"], childColumns = ["route_grade"])
    ],
    indices = [Index(value = ["route_grade"])]
)
data class Attempt(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "climb_style") var climbStyle: String,
    var outcome: String,
    @ColumnInfo(name = "route_grade") var routeGrade: Long,
    @ColumnInfo(name = "route_type") var routeType: String,

    var datetime: LocalDateTime,
    @ColumnInfo(name = "route_name") var routeName: String? = null,
    var comment: String? = null,
    var rating: Int? = null,
    var routeCharacteristics: List<String>? = null,

    var length: Int? = null,

    @Embedded var location: Location? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Attempt

        if (climbStyle != other.climbStyle) return false
        if (outcome != other.outcome) return false
        if (routeGrade != other.routeGrade) return false
        if (routeType != other.routeType) return false
        if (datetime != other.datetime) return false
        if (routeName != other.routeName) return false
        if (comment != other.comment) return false
        if (rating != other.rating) return false
        if (routeCharacteristics != other.routeCharacteristics) return false
        if (length != other.length) return false
        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = climbStyle.hashCode()
        result = 31 * result + outcome.hashCode()
        result = 31 * result + routeGrade.hashCode()
        result = 31 * result + routeType.hashCode()
        result = 31 * result + datetime.hashCode()
        result = 31 * result + (routeName?.hashCode() ?: 0)
        result = 31 * result + (comment?.hashCode() ?: 0)
        result = 31 * result + (rating ?: 0)
        result = 31 * result + routeCharacteristics.hashCode()
        result = 31 * result + (length ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        return result
    }
}
