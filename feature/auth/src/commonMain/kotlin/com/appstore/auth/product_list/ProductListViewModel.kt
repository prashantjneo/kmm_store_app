package com.appstore.auth.product_list

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

    var uiState by mutableStateOf(ProductListUiState())
        private set


    fun getProducts() {

        viewModelScope.launch {

            uiState = uiState.copy(
                requestState = RequestState.Loading
            )

            val first = repository.getProducts()

            val finalResult = if (first is RequestState.Error) {
                delay(400)   // small retry delay
                repository.getProducts()

            } else {
                first
            }

            uiState = uiState.copy(
                requestState = finalResult
            )
        }
    }

}