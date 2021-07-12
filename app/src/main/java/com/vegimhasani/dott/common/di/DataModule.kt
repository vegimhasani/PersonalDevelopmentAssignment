package com.vegimhasani.dott.common.di

import com.vegimhasani.dott.common.data.repository.FoursquareRepository
import com.vegimhasani.dott.common.data.service.FoursquareService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(FoursquareService::class.java)

    @Provides
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideRepository(foursquareService: FoursquareService, coroutineDispatcher: CoroutineDispatcher): FoursquareRepository =
        FoursquareRepository(foursquareService, coroutineDispatcher)
}
