package com.jango.turtle.di

import android.arch.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass
/**
 * Created by Ashwani on 15/06/18.
 */
@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
