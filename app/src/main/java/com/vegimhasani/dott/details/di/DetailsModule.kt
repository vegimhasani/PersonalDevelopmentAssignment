package com.vegimhasani.dott.details.di

import com.vegimhasani.dott.common.data.repository.FoursquareRepository
import com.vegimhasani.dott.details.usecases.GetSingleRestaurant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object DetailsModule {

    @Provides
    fun provideGetSingleRestaurantsUseCase(repository: FoursquareRepository): GetSingleRestaurant = GetSingleRestaurant(repository)
}