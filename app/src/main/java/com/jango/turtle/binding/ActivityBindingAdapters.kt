package com.jango.turtle.binding

import android.databinding.BindingAdapter
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import javax.inject.Inject


/**
 * Created by Ashwani on 14/06/18.
 * Binding adapters that work with a activity instance.
 */
class ActivityBindingAdapters @Inject constructor(val appCompatActivity: AppCompatActivity) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
//        Glide.with(appCompatActivity).load(url).into(imageView)
        Glide.with(appCompatActivity).load(url).apply(RequestOptions().circleCrop()).into(imageView)
    }
}
