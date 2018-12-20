package org.gnvo.climb.tracking.climbtracker.data.room.pojo

import android.arch.persistence.room.Embedded

data class AttemptWithLocation(
    @Embedded val attempt: Attempt,
    @Embedded val location: Location? = null
)