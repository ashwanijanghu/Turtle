package com.jango.turtle.db

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.util.ArrayMap
import com.jango.turtle.vo.Places
import com.jango.turtle.vo.PlacesSearchResult

/**
 * Created by Ashwani on 15/06/18.
 */
@Dao
abstract class PlacesDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg place: Places)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPlaces(places: List<Places>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(result: PlacesSearchResult)

    @Query("SELECT * FROM PlacesSearchResult WHERE `query` = :query")
    abstract fun search(query: String): LiveData<PlacesSearchResult>

    @Query("SELECT * FROM PlacesSearchResult WHERE `query` = :query")
    abstract fun findSearchResult(query: String): PlacesSearchResult?

    fun loadOrdered(placesIds: List<String>): LiveData<List<Places>> {
        val order = ArrayMap<String,Int>()
        placesIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return Transformations.map(loadById(placesIds)) { repositories ->
            /*Collections.sort(repositories) { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            }*/
            repositories
        }
    }

    @Query("SELECT * FROM Places WHERE id in (:placeIds)")
    protected abstract fun loadById(placeIds: List<String>): LiveData<List<Places>>

    @Query("SELECT * FROM Places WHERE id in (:placeId)")
    abstract fun loadPlaceById(placeId: String): LiveData<Places>

    @Query("UPDATE Places SET isFav = :isFav WHERE id =:id")
    abstract fun updateFav(isFav: Boolean,id: String)
}