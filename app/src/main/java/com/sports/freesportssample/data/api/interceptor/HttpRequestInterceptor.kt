package com.sports.freesportssample.data.api.interceptor

import com.sports.freesportssample.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HttpRequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val currentUrl = chain.request().url
            val newUrl = currentUrl.newBuilder()
                .setPathSegment(3, BuildConfig.API_KEY)
                .build()
            val currentRequest = chain.request().newBuilder()
            val newRequest = currentRequest.url(newUrl).build()
            return chain.proceed(newRequest)
        }
}