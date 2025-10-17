package com.example.stockputawayapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Define database access operations
@Dao
interface ProductDao {

    // Show a list of products on the home screen
    // Show only products with null location with remaining quantities
    @Query("SELECT * FROM products WHERE quantity > 0 AND location IS NULL ORDER BY productId ASC")
    fun getAllProductsWithNullLocation(): Flow<List<Product>>


    // A list of products with all locations including null locations (but exclude null location with 0 qty)
    // Used for putaway screen
    @Query("SELECT * FROM products WHERE quantity > 0 ORDER BY productId ASC")
    fun getAllProducts(): Flow<List<Product>>


    // Show a single product on the putaway screen
    @Query("SELECT * FROM products WHERE productId = :productId")
    fun getProductById(productId: String): Flow<Product>


//    @Update(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun updateProduct(product: Product)

// update qty in null location
    @Query("""
        UPDATE products 
        SET quantity = :quantity
        WHERE productId = :productId AND location is null
    """)
    suspend fun updateQuantityInNullLocation(
        productId: String,
        quantity: Int  // update with a remaining qty
    )

    //Update the qty and location of product
    //Scenario for updating a line with null location and existing shelf location
    @Query("""
        UPDATE products 
        SET quantity = :quantity
        WHERE productId = :productId AND location = :selectedLocation
    """)
    suspend fun updateQuantityInShelfLocation(
        productId: String,
        quantity: Int,  // when there is an existing location, pass a new qty
        selectedLocation: String
    )



    //Insert a new product with a new location
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: Product)


}