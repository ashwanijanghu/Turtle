package com.jango.turtle.ui.search

import com.jango.turtle.vo.Places

/**
 * Created by Ashwani on 18/06/18.
 */
interface FavListener {
    fun addFav(place: Places?)
    fun removeFav(place: Places?)
}