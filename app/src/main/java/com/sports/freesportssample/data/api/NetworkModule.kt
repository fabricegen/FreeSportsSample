package com.sports.freesportssample.data.api

import com.sports.freesportssample.BuildConfig
import com.sports.freesportssample.data.api.exception.ApiExceptionCallAdapterFactory
import com.sports.freesportssample.data.api.interceptor.HttpRequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun okHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(HttpRequestInterceptor())
    }

    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        val contentType = "application/json; charset=UTF8".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory(contentType))
    }

    @Provides
    @Singleton
    fun provideFreeSportsApiService(
        okHttpClient: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder
    ): FreeSportsApiService {
        retrofitBuilder.addCallAdapterFactory(ApiExceptionCallAdapterFactory())
        val retrofit = retrofitBuilder
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient.build())
            .build()

        return retrofit.create(FreeSportsApiService::class.java)
    }
}
