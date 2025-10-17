package com.example.stockputawayapp.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stockputawayapp.StockPutawayApplication
import com.example.stockputawayapp.ui.home.HomeScreenViewModel
import com.example.stockputawayapp.ui.putaway.PutawayViewModel
import com.example.stockputawayapp.ui.settings.SettingsViewModel


object AppViewModelProvider {

    val Factory = viewModelFactory {

        // Initializer for HomeScreenViewModel
        initializer {
            HomeScreenViewModel(
                StockPutawayApplication().container.productsRepository,
                StockPutawayApplication().container.settingsRepository
            )
        }
        // Initializer for SettingsViewModel
        initializer {
            SettingsViewModel(
                StockPutawayApplication().container.settingsRepository
            )

        }
        // Initializer for PutawayViewModel
        initializer {
            PutawayViewModel(
                productsRepository = StockPutawayApplication().container.productsRepository,
                locationsRepository = StockPutawayApplication().container.locationsRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
    }
}

// Extension function to queries for [Application] object and
// returns an instance of StockPutawayApplication
fun CreationExtras.StockPutawayApplication(): StockPutawayApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as StockPutawayApplication)