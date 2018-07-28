package com.jango.turtle.di

import android.app.Application
import android.arch.persistence.room.Room
import com.jango.turtle.api.FourSquareService
import com.jango.turtle.db.PlacesDb
import com.jango.turtle.db.PlacesDao
import com.jango.turtle.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
/**
 * Created by Ashwani on 15/06/18.
 */
@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideFourSquareService(): FourSquareService {
        return Retrofit.Builder()
                .baseUrl("https://api.foursquare.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(FourSquareService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): PlacesDb {
        return Room
            .databaseBuilder(app, PlacesDb::class.java, "places.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePlacesDao(db: PlacesDb): PlacesDao {
        return db.placesDao()
    }
}
