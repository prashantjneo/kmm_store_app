package com.appstore.data.domain.model.product_update

import kotlinx.serialization.Serializable
@Serializable
data class UpdateProductRequest(
    val title: String? = null,
    val price: Double? = null,
    val description: String? = null,
    val category: String? = null,
    val image: String? = null
)
