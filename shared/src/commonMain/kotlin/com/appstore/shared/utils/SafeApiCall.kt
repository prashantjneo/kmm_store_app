package com.appstore.shared.utils

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess





suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> HttpResponse,
    crossinline parser: suspend (HttpResponse) -> T
): RequestState<T> {

    return try {

        val response = apiCall()





        if (response.status.isSuccess()) {

            val parsed = parser(response)

            AppLogger.d("API_DEBUG", "Parsed → $parsed")

            RequestState.Success(parsed)

        } else {

            val errorBody = response.bodyAsText()

            AppLogger.e("API_DEBUG", "Error Body → $errorBody")

            RequestState.Error(errorBody)
        }

    } catch (e: Exception) {

        AppLogger.e("API_DEBUG", e.message ?: "Unknown Error")

        RequestState.Error(e.message ?: "Unknown error")
    }
}



/*
suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> HttpResponse,
    crossinline parser: suspend (HttpResponse) -> T
): RequestState<T> {

    return try {

        val response = apiCall()

        if (response.status.isSuccess()) {

            RequestState.Success(parser(response))

        } else {

            RequestState.Error(response.bodyAsText())
        }

    } catch (e: Exception) {

        RequestState.Error(e.message ?: "Unknown error")
    }
}
*/
