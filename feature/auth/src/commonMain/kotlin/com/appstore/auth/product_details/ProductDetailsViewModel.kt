package com.appstore.auth.product_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstore.data.domain.model.login.product_list.ProductResponse
import com.appstore.data.domain.model.product_update.UpdateProductRequest
import com.appstore.data.domain.repository.ProductRepository
import com.appstore.shared.utils.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ProductDetailUiState(
    val requestState: RequestState<ProductResponse> = RequestState.Idle
)

class ProductDetailViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    var uiState by mutableStateOf(ProductDetailUiState())
        private set

    var productDetailState by mutableStateOf<RequestState<ProductResponse>>(
        RequestState.Idle
    )
        private set

    var updateProductState by mutableStateOf<RequestState<ProductResponse>>(
        RequestState.Idle
    )
        private set


    fun getProductDetail(productId: Int) {

        viewModelScope.launch {

            uiState = uiState.copy(
                requestState = RequestState.Loading
            )

            val first = repository.getProductById(productId)

            val finalResult = if (first is RequestState.Error) {
                delay(400)
                repository.getProductById(productId)
            } else {
                first
            }

            uiState = uiState.copy(
                requestState = finalResult
            )
        }
    }


    fun updateProduct(
        productId: Int,
        title: String,
        price: String,
        description: String,
        category: String,
        image: String
    ) {

        val request = UpdateProductRequest(
            title = title,
            price = price.toDoubleOrNull(),
            description = description,
            category =category,
            image =image

        )

        viewModelScope.launch {

            updateProductState = RequestState.Loading

            updateProductState =
                repository.updateProduct(productId, request)
        }
    }

}
