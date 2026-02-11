package com.appstore.data.domain

import com.appstore.data.data.ProductApi
import com.appstore.data.domain.model.login.product_list.ProductResponse
import com.appstore.data.domain.repository.ProductRepository
import com.appstore.shared.utils.safeApiCall
import io.ktor.client.call.body

class ProductRepositoryImpl(private val api: ProductApi) : ProductRepository {

    override suspend fun getProducts() = safeApiCall(
        apiCall = { api.getProducts() },
        parser = { it.body<List<ProductResponse>>() }
    )
}