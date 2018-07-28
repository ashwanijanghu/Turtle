package com.jango.turtle.vo

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import com.google.gson.annotations.SerializedName

data class Venue(
        @field:SerializedName("name")
         val name: String,
        @field:SerializedName("location")
        @field:Embedded(prefix = "location_")
         val location: Location,
        @field:SerializedName("id")
         val id: String)