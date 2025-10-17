package com.example.stockputawayapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    val lotNumber: String?,
    val productName: String?,
    val department: String?,
    val quantity: Int?,
    val location: String?
)

data class ProductDetails(
    val id: Int = 0,
    val productId: String = "",
    val lotNumber: String? = null,
    val productName: String? = null,
    val department: String? = null,
    val quantity: Int? = null,
    val location: String? = null
)

// Extension function to convert Product entity to ProductDetails
fun Product.toProductDetails(): ProductDetails {
    return ProductDetails (
        id = id,
        productId = productId,
        lotNumber = lotNumber,
        productName = productName,
        department = department,
        quantity = quantity,
        location = location
    )
}