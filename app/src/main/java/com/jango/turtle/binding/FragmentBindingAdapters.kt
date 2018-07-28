package com.jango.turtle.binding

import android.databinding.BindingAdapter
import android.support.v4.app.Fragment
import android.widget.ImageView

import com.bumptech.glide.Glide

import javax.inject.Inject
/**
 * Created by Ashwani on 14/06/18.
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        Glide.with(fragment).load(url).into(imageView)
    }
}
