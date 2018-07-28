package com.jango.turtle.ui.common

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng

/**
 * Created by Ashwani on 18/06/18.
 */
data class PlaceDetails(
        val id:String,
        val position: LatLng,
        val title: String = "Marker",
        val snippet: String? = null,
        val icon: BitmapDescriptor = BitmapDescriptorFactory.defaultMarker(),
        val infoWindowAnchorX: Float = 0.5F,
        val infoWindowAnchorY: Float = 0F,
        val draggable: Boolean = false,
        val zIndex: Float = 0F)