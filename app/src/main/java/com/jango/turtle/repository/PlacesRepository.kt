package com.jango.turtle.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.jango.turtle.AppExecutors
import com.jango.turtle.api.ApiSuccessResponse
import com.jango.turtle.api.FourSquareService
import com.jango.turtle.api.places.PlacesResponse
import com.jango.turtle.db.PlacesDao
import com.jango.turtle.db.PlacesDb
import com.jango.turtle.util.AbsentLiveData
import com.jango.turtle.vo.Places
import com.jango.turtle.vo.PlacesSearchResult
import com.jango.turtle.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashwani on 18/06/18.
 * Repository that handles Places instances.
 */
@Singleton
class PlacesRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val db: PlacesDb,
        private val placesDao: PlacesDao,
        private val fourSquareService: FourSquareService
) {
    fun searchPlaces(query: String, clientId:String, clientSecret:String,
                     ll:String,llAcc:Double): LiveData<Resource<List<Places>>> {
        return object : NetworkBoundResource<List<Places>, PlacesResponse>(appExecutors) {

            override fun saveCallResult(item: PlacesResponse) {
                val placedIds = item.response.group.results?.map { it.id }
                if(placedIds != null) {
                    if (placedIds.isNotEmpty()) {
                        val placesSearchResult = PlacesSearchResult(
                                query = query,
                                placesIds = placedIds,
                                totalCount = item.response.group.totalResults
                        )

                        db.beginTransaction()
                        try {
                            placesDao.insertPlaces(item.response.group.results)
                            placesDao.insert(placesSearchResult)
                            db.setTransactionSuccessful()
                        } finally {
                            db.endTransaction()
                        }
                    }
                }
            }

            override fun shouldFetch(data: List<Places>?) = data == null

            override fun loadFromDb(): LiveData<List<Places>> {
                return Transformations.switchMap(placesDao.search(query)) { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        placesDao.loadOrdered(searchData.placesIds)
                    }
                }
            }

            override fun createCall() = fourSquareService.searchPlaces(query,query,
                    clientId,clientSecret,ll,llAcc)

            override fun processResponse(response: ApiSuccessResponse<PlacesResponse>)
                    : PlacesResponse {
                val body = response.body
                return body
            }
        }.asLiveData()
    }

    fun updateFav(isFav:Boolean, id:String){
        appExecutors.diskIO().execute {
            placesDao.updateFav(isFav,id)
        }
    }

    fun loadPlaceData(id: String):LiveData<Places>{
        return placesDao.loadPlaceById(id)
    }

}
