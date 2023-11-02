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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://aeox4la6gs.apigw.ntruss.com/custom/v1/"

    @OptIn(ExperimentalSerializationApi::class)
    private fun provideRetrofit(appContext: Context): Retrofit {
        val chuckerInterceptor = ChuckerInterceptor.Builder(appContext)
            .collector(ChuckerCollector(appContext))
            .alwaysReadResponseBody(true)
            .build()

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC // 필요한 레벨로 변경
        }

        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(chuckerInterceptor)
            connectTimeout(30, TimeUnit.SECONDS) // 연결 시간 제한을 조정합니다.
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }

        val json = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaTypeOrNull()!!))
            .client(httpClient.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideMyMapApi(
        @ApplicationContext appContext: Context
    ): OliveApi = provideRetrofit(appContext).create(OliveApi::class.java)
}