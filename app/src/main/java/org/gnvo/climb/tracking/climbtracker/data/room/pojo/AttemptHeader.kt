package org.gnvo.climb.tracking.climbtracker.data.room.pojo

class AttemptHeader (
    val date: String
) : AttemptListItem() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttemptHeader

        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }

    override fun itemsAreEqualOrHaveSameId(other: AttemptListItem?): Boolean {
        return equals(other)
    }
}
