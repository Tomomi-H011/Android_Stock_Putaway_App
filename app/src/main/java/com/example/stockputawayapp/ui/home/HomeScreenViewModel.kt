package com.example.stockputawayapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockputawayapp.data.Department
import com.example.stockputawayapp.data.Product
import com.example.stockputawayapp.data.ProductsRepository
import com.example.stockputawayapp.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


data class HomeUiState(
    val productList: List<Product> = listOf(),
    val selectedDepartment: Department? = null
)

// ViewModel to retrieve Product data from the Room database

class HomeScreenViewModel (
    private val productsRepository: ProductsRepository,
    private val settingsRepository: SettingsRepository

) : ViewModel() {

    val uiState = combine(
        productsRepository.getAllProductsWithNullLocation(), // Flow<List<Product>>
        settingsRepository.selectedDepartment  // Flow<Department?>
    ) { products, selectedDepartment ->
        val filteredProducts = if (selectedDepartment == null || selectedDepartment == Department.ALL_DEPARTMENTS) {
            products
        } else {
            products.filter {
                it.department.equals(selectedDepartment.name, ignoreCase = true)
            }
        }
        HomeUiState(
            productList = filteredProducts,
            selectedDepartment = selectedDepartment
        )
    }
//        .map { HomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )

}


//    companion object {
//        private const val TIMEOUT_MILLIS = 5_000L
//    }
//
//    val homeUiState: StateFlow<HomeUiState> =
//        productsRepository.getAllProducts().map { HomeUiState(it) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = HomeUiState()
//            )


