package com.appstore.data.data


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.appstore.database.Product
import com.appstore.database.ProductQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class ProductLocalDataSource(
    private val queries: ProductQueries
) {

    fun observeProducts(): Flow<List<Product>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    fun getProductById(id: Long): Flow<List<Product>> {
        return queries.selectById(id)
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    fun insertProducts(products: List<Product>) {
        queries.transaction {
            products.forEach {
                queries.insertProduct(
                    id = it.id,
                    title = it.title,
                    price = it.price,
                    description = it.description,
                    category = it.category,
                    image = it.image
                )
            }
        }
    }

    fun clear() {
        queries.deleteAll()
    }

    fun deleteProductById(id: Long) {
        queries.deleteById(id)
    }

    fun insertProduct(product: Product) {
        queries.insertProduct(
            id = product.id,
            title = product.title,
            price = product.price,
            description = product.description,
            category = product.category,
            image = product.image
        )
    }
}