package com.appstore.data.data

import com.appstore.data.domain.model.login.LoginRequest
import com.appstore.shared.Constants
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApi(
    private val httpClient: HttpClient
) {

    suspend fun login(request: LoginRequest): HttpResponse {

        return httpClient.post(Constants.LOGIN) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}