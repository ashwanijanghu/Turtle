package com.jango.turtle.ui.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.content.res.Resources
import com.jango.turtle.R
import com.jango.turtle.repository.PlacesRepository
import com.jango.turtle.util.AbsentLiveData
import com.jango.turtle.vo.Places
import com.jango.turtle.vo.Resource
import java.util.Locale
import javax.inject.Inject
import com.jango.turtle.ui.common.PlaceDetails

/**
 * Created by Ashwani on 11/06/18.
 */
class SearchViewModel @Inject constructor(val placesRepository: PlacesRepository) : ViewModel() {

    val placesQuery = MutableLiveData<String>()
    lateinit var resources:Resources
    var markerTracker = HashMap<String, PlaceDetails>()
    var placesList:List<Places> = mutableListOf()

    val placesResult:LiveData<Resource<List<Places>>> = Transformations
            .switchMap(placesQuery){ search->
                if(search.isNullOrBlank()){
                    AbsentLiveData.create()
                }else{
                    placesRepository.searchPlaces(search,
                            resources.getString(R.string.foursquare_client_id),
                            resources.getString(R.string.foursquare_client_secret),
                            resources.getString(R.string.seattle_lat_long),
                            500.0)
                }
            }

    fun setPlacesQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == placesQuery.value) {
            return
        }
        placesQuery.value = input
    }

    fun updateFav(isFav: Boolean, id: String?) {
        placesRepository.updateFav(isFav, id!!)
    }
}
