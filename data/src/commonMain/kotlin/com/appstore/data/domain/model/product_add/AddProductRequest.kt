package com.appstore.data.domain.model.product_add

import kotlinx.serialization.Serializable

@Serializable
data class AddProductRequest(
    val title: String,
    val price: Double?,
    val description: String,
    val image: String,
    val category: String
)

