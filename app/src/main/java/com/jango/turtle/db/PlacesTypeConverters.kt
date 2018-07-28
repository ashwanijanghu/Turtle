package com.jango.turtle.db

import android.arch.persistence.room.TypeConverter
import timber.log.Timber
/**
 * Created by Ashwani on 15/06/18.
 */
object PlacesTypeConverters {
    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String?): List<Int>? {
        return data?.let {
            it.split(",").map {
                try {
                    it.toInt()
                } catch (ex: NumberFormatException) {
                    Timber.e(ex, "Cannot convert $it to number")
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(ints: List<Int>?): String? {
        return ints?.joinToString(",")
    }

    @TypeConverter
    @JvmStatic
    fun stringToStringList(data: String?): List<String>? {
        return data?.let {
            it.split(",").map {
                try {
                    it
                } catch (ex: NumberFormatException) {
                    Timber.e(ex, "Cannot convert $it to number")
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun stringListToString(ints: List<String>?): String? {
        return ints?.joinToString(",")
    }

    /*@TypeConverter
    fun categoryFromString(catStr: String?): CategoryEntity? {
        val type = object : TypeToken<CategoryEntity?>() {}.type
        return Gson().fromJson(catStr, type)
    }

    @TypeConverter
    fun categoryToString(categoryEntity: CategoryEntity?): String {
        return Gson().toJson(categoryEntity)
    }*/
}
