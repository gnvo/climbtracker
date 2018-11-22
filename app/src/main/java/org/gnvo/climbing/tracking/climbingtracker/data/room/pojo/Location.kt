package org.gnvo.climbing.tracking.climbingtracker.data.room.pojo

data class Location (
    var latitude: Double? = null,
    var longitude: Double? = null,
    var area: String? = null,
    var sector: String? = null
){
    override fun equals(other: Any?): Boolean {
        return other is Location &&
                latitude == other.latitude &&
                longitude == other.longitude &&
                area == other.area &&
                sector == other.sector
    }
}