package com.appstore.data.domain.repository

import com.appstore.data.domain.model.login.product_list.ProductResponse
import com.appstore.shared.utils.RequestState

interface ProductRepository {
    suspend fun getProducts(): RequestState<List<ProductResponse>>

}