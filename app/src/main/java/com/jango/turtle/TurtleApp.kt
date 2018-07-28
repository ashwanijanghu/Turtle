package com.jango.turtle

import android.app.Activity
import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.jango.turtle.di.AppInjector
import com.facebook.stetho.Stetho
import com.jango.turtle.util.NightModeUtil
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashwani on 11/06/18.
 */
class TurtleApp : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppInjector.init(this)
        Stetho.initializeWithDefaults(this)
        initNightMode()
    }

    override fun activityInjector() = dispatchingAndroidInjector

    private fun initNightMode() {
        if (NightModeUtil.getNightModePref(this)!!) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun toggleNightMode() {
        NightModeUtil.toggleNightModePref(this)
        initNightMode()
    }
}
