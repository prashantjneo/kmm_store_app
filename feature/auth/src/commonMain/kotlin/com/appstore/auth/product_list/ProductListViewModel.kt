package com.appstore.auth.product_list

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



data class ProductUiModel(
    val id: Int,
    val title: String,
    val price: String,
    val description: String,
    val imageUrl: String
)

data class ProductListUiState(
    val requestState: RequestState<List<ProductResponse>> = RequestState.Idle
) {
    val isLoading: Boolean
        get() = requestState.isLoading()
}

class ProductListViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // -------------------------
    // LIST STATE
    // -------------------------
    var uiState by mutableStateOf(ProductListUiState())
        private set

    // -------------------------
    // DETAIL STATE
    // -------------------------
    var productDetailState by mutableStateOf<RequestState<ProductResponse>>(RequestState.Idle)
        private set

    // -------------------------
    // UPDATE STATE
    // -------------------------
    var updateProductState by mutableStateOf<RequestState<ProductResponse>>(RequestState.Idle)
        private set


    // -------------------------
    // GET PRODUCT LIST
    // -------------------------
    fun getProducts() {

        viewModelScope.launch {

            uiState = uiState.copy(requestState = RequestState.Loading)

            val first = repository.getProducts()

            val finalResult = if (first is RequestState.Error) {
                delay(400)
                repository.getProducts()
            } else first

            uiState = uiState.copy(requestState = finalResult)
        }
    }

    // -------------------------
    // GET PRODUCT DETAIL
    // -------------------------
    fun getProductDetail(productId: Int) {

        viewModelScope.launch {

            productDetailState = RequestState.Loading

            val first = repository.getProductById(productId)

            val finalResult = if (first is RequestState.Error) {
                delay(400)
                repository.getProductById(productId)
            } else first

            productDetailState = finalResult
        }
    }

    private var shouldRefreshList = false

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
            category = category,
            image = image
        )

        viewModelScope.launch {

            updateProductState = RequestState.Loading

            val result = repository.updateProduct(productId, request)

            updateProductState = result

            if (result is RequestState.Success) {
                shouldRefreshList = true
            }
        }
    }


    fun refreshIfNeeded() {

        if (shouldRefreshList) {
            shouldRefreshList = false
            print(" API Call from refresh")
            getProducts()
        }
    }


}