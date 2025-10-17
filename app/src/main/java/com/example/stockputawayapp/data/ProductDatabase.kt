package com.example.stockputawayapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


// Create a Room database
@Database(
    entities = [Product::class, Location::class],
    version = 1,
    exportSchema = false
)
abstract class ProductsDatabase : RoomDatabase() {

    // Expose the DAO
    abstract fun productDao(): ProductDao
    abstract fun locationDao(): LocationDao

    companion object {

        // Singleton instance of the database
        @Volatile
        private var INSTANCE: ProductsDatabase? = null // Create a reference to db instance and start as null

        fun getDatabase(context: Context): ProductsDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ProductsDatabase::class.java,
                    "products_database"
                )
                    .createFromAsset("database/products.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }

    }
}