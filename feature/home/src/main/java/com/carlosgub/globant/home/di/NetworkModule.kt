package com.carlosgub.globant.home.di

import com.carlosgub.globant.home.data.network.clients.SearchClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HomeModule {
    @Singleton
    @Provides
    fun providesSearchClient(retrofit: Retrofit): SearchClient =
        retrofit.create(SearchClient::class.java)
}
