package com.appstore.data.data

import com.appstore.shared.Constants
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class ProductApi(
    private val httpClient: HttpClient
) {

    suspend fun getProducts(): HttpResponse {
        return httpClient.get(Constants.PRODUCTLIST)
    }
}