package com.jango.turtle.ui.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.content.res.Resources
import com.jango.turtle.repository.PlacesRepository
import com.jango.turtle.vo.Places
import javax.inject.Inject
/**
 * Created by Ashwani on 18/06/18.
 */
class DetailsViewModel @Inject constructor(val placesRepository: PlacesRepository) : ViewModel() {

    lateinit var resources:Resources

    private val placeId = MutableLiveData<String>()
    var placesDetail:Places? = null

    val placeData = Transformations.switchMap(placeId) {
            placesRepository.loadPlaceData(it)
    }

    fun setPlaceId(input: String) {
        placeId.value = input
    }

    fun updateFav(isFav: Boolean, id: String?) {
        placesRepository.updateFav(isFav, id!!)
    }
}
