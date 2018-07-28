package com.jango.turtle.vo

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.text.TextUtils
import com.google.gson.annotations.SerializedName

@Entity(
        indices = [(Index("id"))],
        primaryKeys = ["id"]
)
data class Places(
        @field:SerializedName("venue")
        @field:Embedded(prefix = "venue_")
        val venue: Venue,
        @field:SerializedName("photo")
        @field:Embedded(prefix = "photo_")
        val photo: Photo?,
        @field:SerializedName("id")
        val id: String,
        @field:SerializedName("isFav")
        val isFav: Boolean){

    data class Photo(@field:SerializedName("createdAt")
                     val createdAt: Int?,
                     @field:SerializedName("visibility")
                     val visibility: String?,
                     @field:SerializedName("prefix")
                     val prefix: String?,
                     @field:SerializedName("width")
                     val width: Int?,
                     @field:SerializedName("id")
                     val id: String?,
                     @field:SerializedName("suffix")
                     val suffix: String?,
                     @field:SerializedName("height")
                     val height: Int?){
        fun getPhoto() : String{
            return prefix+width+"x"+height+suffix;
        }
    }

    companion object {
        const val UNKNOWN_ID = -1
    }

    fun getDistance():String{
        return if(TextUtils.isEmpty(venue.location.distance.toString())) "" else venue.location.distance.toString()+" m"
    }

    fun getAddress():String{
        var fa = venue.location.address.toString()
        fa = if(fa.isNullOrBlank() || fa.isNullOrEmpty()) "Seattle" else fa
        var sa = venue.location.crossStreet.toString()
        sa = if(sa.isNullOrBlank() || sa.isNullOrEmpty()) "" else " , $sa"
        return (fa+sa)
    }
}