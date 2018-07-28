package com.jango.turtle.api.places

import com.jango.turtle.vo.Places
import com.google.gson.annotations.SerializedName
/**
 * Created by Ashwani on 12/06/18.
 */
data class Group(@SerializedName("totalResults")
                 val totalResults: Int = 0,
                 @SerializedName("results")
                 val results: List<Places>?)