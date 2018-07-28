package com.jango.turtle.util

import android.content.Context
import android.content.res.Configuration

/**
 * Created by Ashwani on 11/06/18.
 */
object NightModeUtil {
    private fun isNightModeActive(context: Context): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    fun toggleNightModePref(context: Context) {
        SharedPreferencesManager(context).putBoolean("nightMode", !getNightModePref(context)!!)
    }

    fun getNightModePref(context: Context): Boolean? {
        return SharedPreferencesManager(context).getBoolean("nightMode", false)
    }
}
