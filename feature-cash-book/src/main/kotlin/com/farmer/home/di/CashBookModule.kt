package com.farmer.home.di

import com.farmer.home.data.remote.CashBookApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CashBookModule {

    private const val BASE_URL = "http://localhost:8080/olivebook/"

    private fun provideRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideCashBookApi(): CashBookApi = provideRetrofit().create(CashBookApi::class.java)
}