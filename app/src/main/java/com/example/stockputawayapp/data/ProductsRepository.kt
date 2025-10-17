package com.example.stockputawayapp.data

import kotlinx.coroutines.flow.Flow

// Repository for bridging ViewModel and DAO, providing an abstraction over data access

interface ProductsRepository {

    fun getAllProductsWithNullLocation(): Flow<List<Product>>

    fun getAllProducts(): Flow<List<Product>>

    fun getProductById(productId: String): Flow<Product>

    suspend fun updateQuantityInNullLocation(
        productId: String,
        quantity: Int
    )

    suspend fun updateQuantityInShelfLocation(
        productId: String,
        quantity: Int,
        selectedLocation: String
    )

    suspend fun insertProduct(product: Product)

}