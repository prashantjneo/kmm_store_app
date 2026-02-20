package com.appstore.data.domain

import com.appstore.data.data.ProductApi
import com.appstore.data.domain.model.login.product_list.ProductResponse
import com.appstore.data.domain.model.product_add.AddProductRequest
import com.appstore.data.domain.model.product_update.UpdateProductRequest
import com.appstore.data.domain.repository.ProductRepository
import com.appstore.shared.utils.safeApiCall
import io.ktor.client.call.body

class ProductRepositoryImpl(private val api: ProductApi) : ProductRepository {

    override suspend fun getProducts() = safeApiCall(
        apiCall = { api.getProducts() },
        parser = { it.body<List<ProductResponse>>() }
    )

    override suspend fun getProductById(productId: Int) = safeApiCall(
        apiCall = { api.getProductById(productId) },
        parser = { it.body<ProductResponse>() }
    )

    override suspend fun updateProduct(
        productId: Int,
        request: UpdateProductRequest
    ) = safeApiCall(

        apiCall = {
            api.updateProduct(productId, request)
        },

        parser = {
            it.body<ProductResponse>()
        }
    )

    override suspend fun deleteProduct(
        productId: Int
    ) = safeApiCall(
        apiCall = {
            api.deleteProduct(productId)
        },
        parser = {
              // FakeStore delete returns body but we don’t need it
        }
    )

    override suspend fun addProduct(
        request: AddProductRequest
    ) = safeApiCall(
        apiCall = { api.addProduct(request) },
        parser = { it.body<ProductResponse>() }
    )


}