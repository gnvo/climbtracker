package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import android.text.Editable

class Utils{
    companion object {
        fun getStringOrNull(text: Editable?): String? {
            return when {
                text.isNullOrEmpty() -> null
                else -> return text.toString()
            }
        }
        private val regexLocationExtractor = """([+-]?(?:[0-9]*[.])?[0-9]+)\s*,\s*([+-]?(?:[0-9]*[.])?[0-9]+)""".toRegex()

        fun extractCoordinates(coordinatesText: String): Pair<MatchGroup?, MatchGroup?> {
            val matchResult = regexLocationExtractor.find(coordinatesText)
            return Pair(matchResult?.groups?.get(1), matchResult?.groups?.get(2))
        }
    }
}