package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import android.text.Editable

class Utils{
    companion object {
        public fun getStringOrNull(text: Editable?): String? {
            return when {
                text.isNullOrEmpty() -> null
                else -> return text.toString()
            }
        }
    }
}