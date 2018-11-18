package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.Date

@Entity(tableName = "climbing_entry")
data class ClimbEntry(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val name: String? = null,
    @Embedded val coordinates: Coordinates? = null,
    val site: String? = null,
    val sector: String? = null,
    val datetime: Date,
    @ColumnInfo(name = "route_type") val routeType: String,
    var rating: Int? = null,
    val comment: String? = null
)
