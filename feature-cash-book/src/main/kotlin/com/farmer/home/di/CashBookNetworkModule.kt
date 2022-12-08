package com.farmer.home.di

import com.farmer.home.data.remote.CashBookApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CashBookNetworkModule {

    // IFCONFIG_ADDRESS_ENVIRONMENT_0 value is different for each device.
    // Use "ipconfig getifaddr en0" command in terminal to get the value.
    private const val IFCONFIG_ADDRESS_ENVIRONMENT_0 = "192.168.0.5"
    private const val BASE_URL = "http://${IFCONFIG_ADDRESS_ENVIRONMENT_0}:8080/olivebook/"

    @OptIn(ExperimentalSerializationApi::class)
    private fun provideRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val httpClient = OkHttpClient.Builder()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(httpLoggingInterceptor)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaTypeOrNull()!!))
            .client(httpClient.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideCashBookApi(): CashBookApi = provideRetrofit().create(CashBookApi::class.java)
}