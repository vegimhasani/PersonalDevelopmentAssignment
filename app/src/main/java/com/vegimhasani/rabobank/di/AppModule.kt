package com.vegimhasani.rabobank.di

import android.content.Context
import com.vegimhasani.rabobank.RabobankApplication
import com.vegimhasani.rabobank.main.csv.CsvReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesApplication(@ApplicationContext context: Context): RabobankApplication {
        return context as RabobankApplication
    }

    @Provides
    fun providesCsvReader(): CsvReader {
        return CsvReader()
    }
}
