package com.vegimhasani.dott.map.di

import com.vegimhasani.dott.common.data.repository.FoursquareRepository
import com.vegimhasani.dott.map.domain.usecases.GetNearbyRestaurants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ActivityComponent::class)
object MapsModule {

    @Provides
    fun provideGetAllRestaurantsUseCase(repository: FoursquareRepository, dispatcher: CoroutineDispatcher): GetNearbyRestaurants =
        GetNearbyRestaurants(repository, dispatcher)
}