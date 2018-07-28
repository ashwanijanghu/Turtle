package com.jango.turtle.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Ashwani on 14/06/18.
 */
class SharedPreferencesManager
    /**
     * Initializes a new [SharedPreferencesManager] object.
     *
     * @param context The application context
     */
    (private val context: Context) {
        val SHARED_PREF_DEFAULT = "app_pref"
        /**
         * Returns the [SharedPreferences] under the specified key.
         *
         * @param key The key used to store the data
         * @return [SharedPreferences]
         */
        fun getSharedPreference(key: String): SharedPreferences {
            return context.getSharedPreferences(key, Context.MODE_PRIVATE)
        }

        fun getDefaultSharedPreference(): SharedPreferences {
            return context.getSharedPreferences(SHARED_PREF_DEFAULT, Context.MODE_PRIVATE)
        }

        fun putString(key: String, value: String) {
            getSharedPreference(SHARED_PREF_DEFAULT).edit().putString(key, value).apply()
        }

        fun getString(key: String, defValue: String): String? {
            return getSharedPreference(SHARED_PREF_DEFAULT).getString(key, defValue)
        }

        fun putBoolean(key: String, value: Boolean?) {
            getSharedPreference(SHARED_PREF_DEFAULT).edit().putBoolean(key, value!!).apply()
        }

        fun getBoolean(key: String, defValue: Boolean?): Boolean? {
            return getSharedPreference(SHARED_PREF_DEFAULT).getBoolean(key, defValue!!)
        }
}