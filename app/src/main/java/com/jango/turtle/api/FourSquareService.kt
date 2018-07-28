package com.jango.turtle.api

import android.arch.lifecycle.LiveData
import com.jango.turtle.api.places.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query
/**
 * Created by Ashwani on 12/06/18.
 * REST API access points
 */
interface FourSquareService {

    @GET("search/recommendations?v=20161101")
    fun searchPlaces(@Query("intent") intentString: String,
                     @Query("query") queryString: String,
                    @Query("client_id") clientID: String,
                    @Query("client_secret") clientSecret: String,
                    @Query("ll") ll: String,
                    @Query("llAcc") llAcc: Double): LiveData<ApiResponse<PlacesResponse>>
}
