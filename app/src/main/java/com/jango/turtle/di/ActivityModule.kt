package com.jango.turtle.di

import com.jango.turtle.ui.detail.DetailsActivity
import com.jango.turtle.ui.search.SearchActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector
/**
 * Created by Ashwani on 15/06/18.
 */
@Suppress("unused")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeSearchActivity(): SearchActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailsActivity(): DetailsActivity
}
