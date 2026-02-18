package com.appstore.auth.product_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstore.data.domain.model.login.product_list.ProductResponse
import com.appstore.data.domain.model.product_add.AddProductRequest
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

}

class ProductListViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // -------------------------
    // SINGLE SOURCE OF TRUTH
    // -------------------------
    private val cachedProducts = mutableListOf<ProductResponse>()

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
    // DELETE STATE
    // -------------------------
    var deleteState by mutableStateOf<RequestState<Unit>>(RequestState.Idle)
        private set

    // -------------------------
    // ADD STATE
    // -------------------------
    var addState by mutableStateOf<RequestState<ProductResponse>>(RequestState.Idle)
        private set


    // ============================================================
    // GET PRODUCTS (ONLY FIRST TIME FROM API)
    // ============================================================
    fun getProducts() {

        viewModelScope.launch {

            // 🔴 If already loaded once, use cache
            if (cachedProducts.isNotEmpty()) {
                uiState = uiState.copy(
                    requestState = RequestState.Success(cachedProducts.toList())
                )
                return@launch
            }

            uiState = uiState.copy(requestState = RequestState.Loading)

            val first = repository.getProducts()

            val finalResult = if (first is RequestState.Error) {
                delay(400)
                repository.getProducts()
            } else first

            if (finalResult is RequestState.Success) {

                cachedProducts.clear()
                cachedProducts.addAll(finalResult.data)

                uiState = uiState.copy(
                    requestState = RequestState.Success(cachedProducts.toList())
                )
            } else {
                uiState = uiState.copy(requestState = finalResult)
            }
        }
    }


    // ============================================================
    // GET PRODUCT DETAIL
    // ============================================================
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


    // ============================================================
    // UPDATE PRODUCT (LOCAL UPDATE AFTER SUCCESS)
    // ============================================================
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

                val index = cachedProducts.indexOfFirst { it.id == productId }

                if (index != -1) {
                    cachedProducts[index] = result.data.copy(id = productId)

                    uiState = uiState.copy(
                        requestState = RequestState.Success(cachedProducts.toList())
                    )
                }
            }
        }
    }


    // ============================================================
    // DELETE PRODUCT (LOCAL REMOVE)
    // ============================================================
    fun deleteProduct(productId: Int) {

        viewModelScope.launch {

            deleteState = RequestState.Loading

            val result = repository.deleteProduct(productId)

            deleteState = result

            if (result is RequestState.Success) {

                cachedProducts.removeAll { it.id == productId }

                uiState = uiState.copy(
                    requestState = RequestState.Success(cachedProducts.toList())
                )
            }
        }
    }


    // ============================================================
    // ADD PRODUCT (LOCAL INSERT)
    // ============================================================
    fun addProduct(
        title: String,
        price: String,
        description: String,
        category: String,
        image: String
    ) {

        val request = AddProductRequest(
            title = title,
            price = price.toDoubleOrNull(),
            description = description,
            category = category,
            image = image
        )

        viewModelScope.launch {

            addState = RequestState.Loading

            val result = repository.addProduct(request)

            addState = result

            if (result is RequestState.Success) {

                // 🔥 ALWAYS build from current cache, not uiState
                val currentList = when (val state = uiState.requestState) {
                    is RequestState.Success -> state.data.toMutableList()
                    else -> mutableListOf()
                }

                // 🔥 guaranteed unique id
                val newId = (currentList.maxOfOrNull { it.id } ?: 0) + 1

                val newItem = result.data.copy(id = newId)
                println("ADD INSERTED -> ${newItem.id} ${newItem.title} ${currentList.size}")


                currentList.add(0, newItem)

                // 🔥 force new state emission
                uiState = ProductListUiState(
                    requestState = RequestState.Success(currentList.toList())
                )

                println("ADD INSERTED -> ${newItem.id} ${newItem.title} ${currentList.size}")
            }
        }
    }

    fun resetUpdateState() {
        updateProductState = RequestState.Idle
    }

}

