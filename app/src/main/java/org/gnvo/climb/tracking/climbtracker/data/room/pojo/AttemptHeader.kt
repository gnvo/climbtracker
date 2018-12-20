package org.gnvo.climb.tracking.climbtracker.data.room.pojo

data class AttemptHeader (
    val date: String
) : AttemptListItem() {
    override fun itemsAreEqualOrHaveSameId(other: AttemptListItem?): Boolean {
        return equals(other)
    }
}
