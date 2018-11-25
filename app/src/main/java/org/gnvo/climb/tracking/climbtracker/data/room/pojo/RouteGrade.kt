package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "route_grade")
data class RouteGrade(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "route_grade_id") var routeGradeId: Long? = null,
    var french: String? = null,
    var yds: String? = null,
    var uiaa: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RouteGrade

        if (french != other.french) return false
        if (yds != other.yds) return false
        if (uiaa != other.uiaa) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(routeGradeId, french, yds, uiaa)
    }

    companion object {
        fun initialData(): List<RouteGrade> {
            return listOf(
                RouteGrade(yds = "5.2", french = "1", uiaa = "I"),
                RouteGrade(yds = "5.3", french = "2", uiaa = "II"),
                RouteGrade(yds = "5.4", french = "3", uiaa = "III"),
                RouteGrade(yds = "5.5", french = "4a", uiaa = "IV-"),
                RouteGrade(yds = "5.6", french = "4b", uiaa = "IV+"),
                RouteGrade(yds = "5.7", french = "4c", uiaa = "V-"),
                RouteGrade(yds = "5.8", french = "5a", uiaa = "V+"),
                RouteGrade(yds = "5.9", french = "5b", uiaa = "VI-"),

                RouteGrade(yds = "5.10a", french = "5c", uiaa = "VI"),
                RouteGrade(yds = "5.10b", french = "6a", uiaa = "VI+"),
                RouteGrade(yds = "5.10c", french = "6a+", uiaa = "VII-"),
                RouteGrade(yds = "5.10d", french = "6b", uiaa = "VII"),

                RouteGrade(yds = "5.11a", french = "6b+", uiaa = "VII+"),
                RouteGrade(yds = "5.11b", french = "6c", uiaa = "VIII-"),
                RouteGrade(yds = "5.11c", french = "6c+", uiaa = "VIII"),
                RouteGrade(yds = "5.11d", french = "7a", uiaa = "VIII+"),

                RouteGrade(yds = "5.12a", french = "7a+", uiaa = "IX-"),
                RouteGrade(yds = "5.12b", french = "7b", uiaa = "IX- IX"),
                RouteGrade(yds = "5.12c", french = "7b+", uiaa = "IX"),
                RouteGrade(yds = "5.12d", french = "7c", uiaa = "IX IX+"),

                RouteGrade(yds = "5.13a", french = "7c+", uiaa = "IX+"),
                RouteGrade(yds = "5.13b", french = "8a", uiaa = "IX+ X-"),
                RouteGrade(yds = "5.13c", french = "8a+", uiaa = "X-"),
                RouteGrade(yds = "5.13d", french = "8b", uiaa = "X"),

                RouteGrade(yds = "5.14a", french = "8b+", uiaa = "X+"),
                RouteGrade(yds = "5.14b", french = "8c", uiaa = "X+ XI-"),
                RouteGrade(yds = "5.14c", french = "8c+", uiaa = "XI-"),
                RouteGrade(yds = "5.14d", french = "9a", uiaa = "XI"),

                RouteGrade(yds = "5.15a", french = "9a+", uiaa = "XI+"),
                RouteGrade(yds = "5.15b", french = "9b", uiaa = "XI+"),
                RouteGrade(yds = "5.15c", french = "9b+", uiaa = "XII"),
                RouteGrade(yds = "5.15d", french = "9c", uiaa = "XII+")
            )
        }
    }
}
