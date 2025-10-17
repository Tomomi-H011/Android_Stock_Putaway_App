package com.example.stockputawayapp.data

import android.content.Context

// App container for Dependency injection
// Any implementing class must provide a ProductsRepository.
interface AppContainer {
    val productsRepository: ProductsRepository
    val settingsRepository: SettingsRepository

    val locationsRepository: LocationsRepository
}


// Concrete implementation of AppContainer that provides app dependencies.
// Uses lazy initialization to create the repository only when first needed.
// Currently, it provides an OfflineProductsRepository backed by Room database.
class AppDataContainer(private val context: Context) : AppContainer {

    override val productsRepository: ProductsRepository by lazy {
        OfflineProductsRepository(
            ProductsDatabase.getDatabase(context).productDao()
        )
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository()

    }

    override val locationsRepository: LocationsRepository by lazy {
        LocationsRepository(
            ProductsDatabase.getDatabase(context).locationDao()
        )

    }

}