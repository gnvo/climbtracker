package org.gnvo.climb.tracking.climbtracker.preferences

import android.content.Context
import android.content.SharedPreferences

class AppPreferencesHelper(
    context: Context,
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
) {
    fun getAlwaysShow(): String? {
        return prefs.getString(PREF_KEY_ALWAYS_SHOW, null)
    }

    fun setAlwaysShow(accessToken: String) {
        prefs.edit().putString(PREF_KEY_ALWAYS_SHOW, accessToken).apply()
    }

    companion object {
        private const val PREFS_NAME = "app_pref"
        private const val PREF_KEY_ALWAYS_SHOW = "PREF_KEY_ALWAYS_SHOW"
    }
}