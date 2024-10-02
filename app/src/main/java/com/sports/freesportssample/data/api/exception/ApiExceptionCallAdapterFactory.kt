package com.sports.freesportssample.data.api.exception

import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * General handler for API exceptions
 */
class ApiExceptionCallAdapterFactory(
) : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        val nextCallAdapter = retrofit.nextCallAdapter(this, returnType, annotations)
        return ApiExceptionCallAdapter(nextCallAdapter)
    }
}

private class ApiExceptionCallAdapter<Response, Type>(
    private val delegate: CallAdapter<Response, Type>
) : CallAdapter<Response, Type> {

    override fun adapt(call: Call<Response>): Type = delegate.adapt(ApiExceptionCall(call))

    override fun responseType(): java.lang.reflect.Type = delegate.responseType()
}

private class ApiExceptionCall<Response>(
    private val delegate: Call<Response>
) : Call<Response> {

    override fun enqueue(callback: Callback<Response>) {
        delegate.enqueue(object : Callback<Response> {
            override fun onFailure(call: Call<Response>, t: Throwable) {
                callback.onFailure(call, t)
            }

            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    callback.onResponse(call, response)
                } else {
                    callback.onFailure(call, mapUnsuccessfulResponseToApiException(response))
                }
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone(): Call<Response> = ApiExceptionCall(delegate.clone())

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): retrofit2.Response<Response> {
        val response = runCatching {
            delegate.execute()
        }.getOrElse {
            throw it
        }

        return if (response.isSuccessful) {
            response
        } else {
            throw mapUnsuccessfulResponseToApiException(response)
        }
    }

    private fun mapUnsuccessfulResponseToApiException(response: retrofit2.Response<Response>): ApiException {
        val errorBodyString = response.errorBody()?.string()
        return try {
            errorBodyString?.let { Json.decodeFromString<ApiException>(it) } ?: ApiException(
                response.code(),
                ""
            )
        } catch (exception: Exception) {
            ApiException(
                response.code(),
                errorBodyString ?: ""
            )
        }
    }

    override fun request(): Request = delegate.request()

    override fun timeout() = Timeout()
}
