package com.jango.turtle.api.places

import com.google.gson.annotations.SerializedName
/**
 * Created by Ashwani on 12/06/18.
 */
data class PlacesResponse(
                          @SerializedName("response")
                          val response: Response)