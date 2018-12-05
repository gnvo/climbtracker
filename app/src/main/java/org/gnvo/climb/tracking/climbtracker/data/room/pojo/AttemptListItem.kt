package org.gnvo.climb.tracking.climbtracker.data.room.pojo

abstract class AttemptListItem {
    abstract fun areItemsTheSame(other: AttemptListItem?): Boolean
}
