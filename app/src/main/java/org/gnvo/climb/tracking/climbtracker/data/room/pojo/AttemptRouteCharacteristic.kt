package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "attempt_route_characteristic",
    foreignKeys = [
        ForeignKey(entity = Attempt::class, parentColumns = ["id"], childColumns = ["attempt"]),
        ForeignKey(entity = RouteCharacteristic::class, parentColumns = ["route_characteristic_id"], childColumns = ["route_characteristic"])
    ]
)
data class AttemptRouteCharacteristic(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "attempt_route_characteristic_id") var attemptRouteCharacteristicId: Long? = null,
    var attempt: Long,
    @ColumnInfo(name = "route_characteristic") var routeCharacteristic: Long
)