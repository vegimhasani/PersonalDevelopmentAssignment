package com.vegimhasani.rabobank.di

import android.content.Context
import com.vegimhasani.rabobank.RabobankApplication
import com.vegimhasani.rabobank.main.csv.CsvReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesApplication(@ApplicationContext context: Context): RabobankApplication {
        return context as RabobankApplication
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }


    @Provides
    fun providesCsvCall(okHttpClient: OkHttpClient): Call {
        val request = Request.Builder().url("https://raw.githubusercontent.com/RabobankDev/AssignmentCSV/main/issues.csv")
            .build()
        return okHttpClient.newCall(request)
    }
    @Provides
    fun providesCsvReader(call: Call): CsvReader {
        return CsvReader(call)
    }
}
