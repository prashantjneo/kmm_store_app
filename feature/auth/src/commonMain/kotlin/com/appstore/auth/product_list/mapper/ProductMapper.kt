package com.appstore.auth.product_list.mapper

import com.appstore.auth.product_list.ProductUiModel
import com.appstore.data.domain.model.login.product_list.ProductResponse

fun ProductResponse.toUiModel(): ProductUiModel {
    return ProductUiModel(
        id = id,
        title = title,
        price = price.toString(),
        description = description,
        imageUrl = image
    )
}