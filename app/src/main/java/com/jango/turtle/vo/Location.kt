package com.jango.turtle.vo

import com.google.gson.annotations.SerializedName

data class Location(@field:SerializedName("cc")
                    val cc: String?,
                    @field:SerializedName("country")
                    val country: String?,
                    @field:SerializedName("address")
                    val address: String?,
                    @field:SerializedName("lng")
                    val lng: Double,
                    @field:SerializedName("distance")
                    val distance: Int,
                    @field:SerializedName("city")
                    val city: String?,
                    @field:SerializedName("state")
                    val state: String?,
                    @field:SerializedName("crossStreet")
                    val crossStreet: String?,
                    @field:SerializedName("lat")
                    val lat: Double?)