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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CashBookNetworkModule {

    // IFCONFIG_ADDRESS_ENVIRONMENT_0 value is different for each device.
    // Use "ipconfig getifaddr en0" command in terminal to get the value.
    //원래 192.168.0.5
    private const val IFCONFIG_ADDRESS_ENVIRONMENT_0 = "192.168.1.208"
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

//@Module
//@InstallIn(SingletonComponent::class)
//internal object CashBookNetworkModule {
//
//    private const val BASE_URL = "http://192.168.1.208:8080/olivebook/" // Your base URL
//
//    private fun provideOkHttpClient(): OkHttpClient {
//        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//        return OkHttpClient.Builder()
//            .addInterceptor(httpLoggingInterceptor)
//            .build()
//    }
//
//    private fun provideRetrofit(): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(provideOkHttpClient())
//            .addConverterFactory(Json.asConverterFactory("- multipart/form-data".toMediaTypeOrNull()!!)) // Use appropriate converter factory for your needs (Moshi, Gson, etc.)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideCashBookApi(): CashBookApi = provideRetrofit().create(CashBookApi::class.java)
//
//}





