package com.appstore.data.data

import com.appstore.data.domain.model.product_update.UpdateProductRequest
import com.appstore.shared.Constants
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProductApi(
    private val httpClient: HttpClient
) {

    suspend fun getProducts(): HttpResponse {
        return httpClient.get(Constants.PRODUCTLIST)
    }

    suspend fun getProductById(productId: Int): HttpResponse {
        return httpClient.get("${Constants.PRODUCTLIST}/$productId")

    }

    suspend fun updateProduct(
        productId: Int,
        request: UpdateProductRequest
    ): HttpResponse {

        return httpClient.put("${Constants.PRODUCTLIST}/$productId") {

            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun deleteProduct(productId: Int): HttpResponse {
        return httpClient.delete("${Constants.PRODUCTLIST}/$productId")
    }


}