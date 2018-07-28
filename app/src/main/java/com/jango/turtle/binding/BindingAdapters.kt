package com.jango.turtle.binding

import android.databinding.BindingAdapter
import android.view.View
/**
 * Created by Ashwani on 14/06/18.
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}
