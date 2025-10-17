package com.example.stockputawayapp.data

import kotlinx.coroutines.flow.Flow

class OfflineProductsRepository(private val productDao: ProductDao) : ProductsRepository {

    override fun getAllProductsWithNullLocation(): Flow<List<Product>> = productDao.getAllProductsWithNullLocation()

    override fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()

    override fun getProductById(productId: String): Flow<Product> = productDao.getProductById(productId)

    override suspend fun updateQuantityInNullLocation(
        productId: String,
        quantity: Int
    ) = productDao.updateQuantityInNullLocation(productId, quantity)

    override suspend fun updateQuantityInShelfLocation(
        productId: String,
        quantity: Int,
        selectedLocation: String
    ) = productDao.updateQuantityInShelfLocation(productId, quantity, selectedLocation)

    override suspend fun insertProduct(product: Product) = productDao.insertProduct(product)

}