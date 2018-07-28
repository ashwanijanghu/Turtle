package com.jango.turtle.util

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A lazy property that gets cleaned up when the activity is destroyed.
 *
 * Accessing this variable in a destroyed activity will throw NPE.
 */
class AutoClearedValueActivity<T : Any>(val appCompatActivity: AppCompatActivity) : ReadWriteProperty<AppCompatActivity, T> {
    override fun setValue(thisRef: AppCompatActivity, property: KProperty<*>, value: T) {
        _value = value
    }

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        return _value ?: throw IllegalStateException(
                "should never call auto-cleared-value get when it might not be available"
        )
    }

    private var _value: T? = null

    init {
        appCompatActivity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                _value = null
            }
        })
    }
}

/**
 * Creates an [AutoClearedValue] associated with this fragment.
 */
fun <T : Any> AppCompatActivity.autoClearedActivity() = AutoClearedValueActivity<T>(this)