package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "climbing_entry")
data class ClimbEntry(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var datetime: LocalDateTime,
    var name: String? = null,
    var area: String? = null,
    var sector: String? = null,
    var comment: String? = null,
    @ColumnInfo(name = "route_type") var routeType: String? = null,
    var rating: Int? = null,
    @Embedded var coordinates: Coordinates? = null
)