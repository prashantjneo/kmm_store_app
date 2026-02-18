package com.appstore.auth.product_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstore.data.domain.model.login.product_list.ProductResponse
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


}
