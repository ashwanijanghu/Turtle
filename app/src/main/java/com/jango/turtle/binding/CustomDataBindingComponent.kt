package com.jango.turtle.binding

import android.databinding.DataBindingComponent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
/**
 * Created by Ashwani on 14/06/18.
 * A Data Binding Component implementation for fragments.
 */
class CustomDataBindingComponent(appCompatActivity: AppCompatActivity,fragment: Fragment) : DataBindingComponent {

    private val adapter = FragmentBindingAdapters(fragment)
    private val activityAdapter = ActivityBindingAdapters(appCompatActivity)

    override fun getFragmentBindingAdapters() = adapter
    override fun getActivityBindingAdapters() = activityAdapter
}
