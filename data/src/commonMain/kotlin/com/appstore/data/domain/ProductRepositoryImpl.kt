package com.appstore.data.domain

import com.appstore.data.data.ProductApi
import com.appstore.data.data.ProductLocalDataSource
import com.appstore.data.domain.model.login.product_list.ProductResponse
import com.appstore.data.domain.model.product_add.AddProductRequest
import com.appstore.data.domain.model.product_update.UpdateProductRequest
import com.appstore.data.domain.repository.ProductRepository
import com.appstore.database.Product
import com.appstore.shared.utils.RequestState
import com.appstore.shared.utils.safeApiCall
import io.ktor.client.call.body
import kotlinx.coroutines.flow.first

class ProductRepositoryImpl(
    private val api: ProductApi,
    private val local: ProductLocalDataSource
) : ProductRepository {

    override suspend fun getProducts(): RequestState<List<ProductResponse>> {

        // 🔹 Try API first (same as before)
        val network = safeApiCall(
            apiCall = { api.getProducts() },
            parser = { it.body<List<ProductResponse>>() }
        )

        return when (network) {

            is RequestState.Success -> {

                // 🔹 Convert API → DB model
                val dbModels = network.data.map {
                    com.appstore.database.Product(
                        id = it.id.toLong(),
                        title = it.title,
                        price = it.price,
                        description = it.description,
                        category = it.category,
                        image = it.image
                    )
                }

                // 🔹 Save into DB
                local.clear()
                local.insertProducts(dbModels)

                println("✅ DB INSERT SUCCESS: ${dbModels.size}")

                network
            }

            is RequestState.Error -> {

                println("🌐 API FAILED, trying DB fallback")

                // 🔹 Load from DB
                val cached = local.observeProducts().first()

                if (cached.isNotEmpty()) {

                    println("📦 Loaded ${cached.size} items from DB")

                    val mapped = cached.map {
                        ProductResponse(
                            id = it.id.toInt(),
                            title = it.title,
                            price = it.price,
                            description = it.description ?: "",
                            category = it.category ?: "",
                            image = it.image ?: ""
                        )
                    }

                    RequestState.Success(mapped)
                } else {
                    network
                }
            }

            else -> network
        }
    }

   /* override suspend fun getProductById(productId: Int) = safeApiCall(
        apiCall = { api.getProductById(productId) },
        parser = { it.body<ProductResponse>() }
    )
*/

    override suspend fun getProductById(
        productId: Int
    ): RequestState<ProductResponse> {

        try {

            // 1️⃣ Try local DB first
            val local = local.getProductById(productId.toLong())

            if (local != null) {
                println("✅ Loaded from local DB")

                return RequestState.Success(
                    ProductResponse(
                        id = local.id.toInt(),
                        title = local.title,
                        price = local.price,
                        description = local.description?:"",
                        category = local.category?:"",
                        image = local.image?:""
                    )
                )
            }

            println("🌍 Not found locally, calling network")

            // 2️⃣ Call network
            val networkResult = safeApiCall(
                apiCall = { api.getProductById(productId) },
                parser = { it.body<ProductResponse>() }
            )

            if (networkResult is RequestState.Success) {

                println("🌍 Network success, saving to DB")

                val data = networkResult.data

                this.local.insertOrReplace(
                    Product(
                        id = data.id.toLong(),
                        title = data.title,
                        price = data.price,
                        description = data.description,
                        category = data.category,
                        image = data.image
                    )
                )
            }

            return networkResult

        } catch (e: Exception) {

            println("❌ Exception in getProductById: ${e.message}")
            return RequestState.Error(e.message ?: "Unknown error")
        }
    }


    /*override suspend fun updateProduct(
        productId: Int,
        request: UpdateProductRequest
    ) = safeApiCall(

        apiCall = {
            api.updateProduct(productId, request)
        },

        parser = {
            it.body<ProductResponse>()
        }
    )*/

    override suspend fun updateProduct(
        productId: Int,
        request: UpdateProductRequest
    ): RequestState<ProductResponse> {

        return try {

            // 1️⃣ LOCAL UPDATE FIRST

            local.updateProduct(
                id = productId.toLong(),
                title = request.title ?: "",
                price = request.price ?: 0.0,
                description = request.description ?: "",
                category = request.category ?: "",
                image = request.image ?: ""
            )

            println("✅ Local DB updated")

            // 2️⃣ NETWORK CALL
            val networkResult = safeApiCall(
                apiCall = { api.updateProduct(productId, request) },
                parser = { it.body<ProductResponse>() }
            )

            when (networkResult) {

                is RequestState.Success -> {
                    println("🌍 Network success")
                    networkResult
                }

                is RequestState.Error -> {
                    println("⚠️ Network failed, returning local success")

                    // IMPORTANT:
                    // return success so your ViewModel updates cachedProducts
                    RequestState.Success(
                        ProductResponse(
                            id = productId,
                            title = request.title ?: "",
                            price = request.price ?: 0.0,
                            description = request.description ?: "",
                            category = request.category ?: "",
                            image = request.image ?: ""
                        )
                    )
                }

                else -> networkResult
            }

        } catch (e: Exception) {
            println("❌ Update exception: ${e.message}")
            RequestState.Error(e.message ?: "Update failed")
        }
    }

    override suspend fun deleteProduct(
        productId: Int
    ): RequestState<Unit> {

        // 🔹 Always delete locally first
        local.deleteProductById(productId.toLong())
        println("🗑 Local delete success")

        // 🔹 Try API (if network available)
        val result = safeApiCall(
            apiCall = { api.deleteProduct(productId) },
            parser = { }
        )

        return when (result) {
            is RequestState.Error -> {
                println("⚠️ API delete failed, but local delete kept")
                RequestState.Success(Unit)
            }

            else -> RequestState.Success(Unit)
        }
    }

    override suspend fun addProduct(
        request: AddProductRequest
    ): RequestState<ProductResponse> {

        // 🔹 Generate local temporary ID
        val localId = kotlin.random.Random.nextInt(1000000, 9999999)
        val localProduct = Product(
            id = localId.toLong(),
            title = request.title,
            price = request.price ?: 0.0,
            description = request.description,
            category = request.category,
            image = request.image
        )

        // 🔹 Insert locally first
        local.insertProduct(localProduct)
        println("➕ Local insert success")

        // 🔹 Try API
        val result = safeApiCall(
            apiCall = { api.addProduct(request) },
            parser = { it.body<ProductResponse>() }
        )

        return when (result) {
            is RequestState.Success -> {
                println("🌐 API add success")
                result
            }

            is RequestState.Error -> {
                println("⚠️ API add failed, local insert kept")

                RequestState.Success(
                    ProductResponse(
                        id = localId,
                        title = request.title,
                        price = request.price ?: 0.0,
                        description = request.description,
                        category = request.category,
                        image = request.image
                    )
                )
            }

            else -> result
        }
    }
}