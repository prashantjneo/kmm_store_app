package com.appstore.data.domain.repository

import com.appstore.data.domain.model.login.product_list.ProductResponse
import com.appstore.data.domain.model.product_update.UpdateProductRequest
import com.appstore.shared.utils.RequestState

interface ProductRepository {
    suspend fun getProducts(): RequestState<List<ProductResponse>>

    suspend fun getProductById(productId: Int): RequestState<ProductResponse>

    suspend fun updateProduct(
        productId: Int,
        request: UpdateProductRequest
    ): RequestState<ProductResponse>


    suspend fun deleteProduct(productId: Int): RequestState<Unit>

}