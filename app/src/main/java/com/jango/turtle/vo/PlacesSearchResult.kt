package com.jango.turtle.vo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.TypeConverters
import com.jango.turtle.db.PlacesTypeConverters

/**
 * Created by Ashwani on 15/06/18.
 */

@Entity(primaryKeys = ["query"])
@TypeConverters(PlacesTypeConverters::class)
data class PlacesSearchResult(
        val query: String,
        val placesIds: List<String>,
        val totalCount: Int
)