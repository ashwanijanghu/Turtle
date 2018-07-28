package com.jango.turtle.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.jango.turtle.vo.*
/**
 * Created by Ashwani on 15/06/18.
 * Main database description.
 */
@Database(
    entities = [
        Places::class,
        PlacesSearchResult::class],
    version = 7,
    exportSchema = false
)
abstract class PlacesDb : RoomDatabase() {
    abstract fun placesDao(): PlacesDao
}
