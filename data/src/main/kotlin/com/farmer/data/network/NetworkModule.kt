package com.farmer.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
object NetworkModule {
    private const val BASE_URL = "https://aeox4la6gs.apigw.ntruss.com/custom/v1/"

    @OptIn(ExperimentalSerializationApi::class)
    private fun provideRetrofit(
        appContext: Context
    ): Retrofit {
        val chuckerInterceptor = ChuckerInterceptor.Builder(appContext)
            .collector(ChuckerCollector(appContext))
            .alwaysReadResponseBody(true)
            .build()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val httpClient = OkHttpClient.Builder()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(httpLoggingInterceptor)
        httpClient.addInterceptor(chuckerInterceptor)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaTypeOrNull()!!))
            .client(httpClient.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideMyMapApi(
        @ApplicationContext appContext: Context
    ): OliveApi = provideRetrofit(appContext).create(OliveApi::class.java)
}