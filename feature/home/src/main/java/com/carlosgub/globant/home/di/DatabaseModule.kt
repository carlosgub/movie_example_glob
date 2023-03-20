package com.carlosgub.globant.home.di

import android.content.Context
import androidx.room.Room
import com.carlosgub.globant.home.data.database.MovieDatabase
import com.carlosgub.globant.home.data.database.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao =
        movieDatabase.movieDao()

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext appContext: Context): MovieDatabase =
        Room.databaseBuilder(appContext, MovieDatabase::class.java, "CATEGORY").build()
}
