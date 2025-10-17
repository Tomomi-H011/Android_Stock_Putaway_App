package com.example.stockputawayapp.ui.putaway

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockputawayapp.data.LocationsRepository
import com.example.stockputawayapp.data.Product
import com.example.stockputawayapp.data.ProductDetails
import com.example.stockputawayapp.data.ProductsRepository
import com.example.stockputawayapp.data.toProductDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

data class PutawayUiState(
    val productDetails: ProductDetails = ProductDetails(),
    val selectedLocation: String = "",
    val availableLocations: List<String> = emptyList(),
)

class PutawayViewModel(
    savedStateHandle: SavedStateHandle,
    private val productsRepository: ProductsRepository,
    private val locationsRepository: LocationsRepository
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle[PutAwayDestination.productIdArg])

    private val _uiState = MutableStateFlow(PutawayUiState())
    val uiState: StateFlow<PutawayUiState> = _uiState

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
//    val allProducts: StateFlow<List<Product>> = _allProducts


    init {
        viewModelScope.launch {
            productsRepository.getProductById(productId)
                .filterNotNull()
                .collect { product ->
                    _uiState.value = _uiState.value.copy(
                        productDetails = product.toProductDetails(),
                    )

                    println("Product Details in Init: ${_uiState.value.productDetails}")

                }


        }
        viewModelScope.launch {
            productsRepository.getAllProducts().collect { products ->
                _allProducts.value = products
                println("All products in Init: ${_allProducts.value}")
            }
        }
    }

    // Fetch available locations for a department
    fun loadLocations(department: String) {
        viewModelScope.launch {
            val locations = locationsRepository.getLocations(department)

            // Update UI with the fetched locations
            _uiState.value = _uiState.value.copy(availableLocations = locations)
        }

    }

    fun selectLocation(location: String) {
        _uiState.value = _uiState.value.copy(selectedLocation = location)
    }

    fun updateQuantity(newQty: Int) {
        _uiState.value =
            _uiState.value.copy(productDetails = _uiState.value.productDetails.copy(quantity = newQty))
    }

    // Update the database with user input
    fun updateQtyAndLoc() {
        val productDetails = _uiState.value.productDetails
        val selectedLocation = _uiState.value.selectedLocation
        val productList = _allProducts.value
        val selectedProductId = productDetails.productId
        val originalLocation = null  // always null
        val originalQtyInNullLocation: Int =
            productList.firstOrNull { (it.productId == selectedProductId && it.location == null) }?.quantity
                ?: 0 //originalQuantity from list
        val originalQuantityInShelf: Int =
            productList.firstOrNull { it.productId == selectedProductId && it.location == selectedLocation }?.quantity
                ?: 0
        var moveQuantity: Int
        val remainingQuantity: Int =
            originalQtyInNullLocation - productDetails.quantity!! // remaining qty in null location



        viewModelScope.launch {

            // First, update null location with a new qty
            productsRepository.updateQuantityInNullLocation(
                productId = selectedProductId,
                quantity = remainingQuantity
            )
            println("Updated null location: Remaining Quantity: $remainingQuantity")
            println("Updated null location: SelectedLocation: $selectedLocation")

            // Then, Case1: if [productId & shelf location] combination already exists -> Update the shelf location with a new qty
            if (!selectedLocation.isBlank()) {
                if (productList.any { (it.productId == selectedProductId) && (it.location == selectedLocation) }) {
                    moveQuantity = (productDetails.quantity ?: 0) + (originalQuantityInShelf)  // Quantity to add to a new location (update qty from ui + qty from database)

                    // Update the existing shelf location
                    productsRepository.updateQuantityInShelfLocation(
                        productId = selectedProductId,
                        quantity = moveQuantity,
                        selectedLocation = selectedLocation,// original shelf location
                    )
                    println("Update Function: Move Quantity: $moveQuantity")

                } else { // Case 2: if [productId & shelf location] combination does not exist -> Insert a new line

                    moveQuantity = productDetails.quantity ?: 0  // Qty from textbox input

                    val newProduct = Product(
                        productId = selectedProductId,
                        lotNumber = productDetails.lotNumber,
                        productName = productDetails.productName,
                        department = productDetails.department,
                        quantity = moveQuantity,
                        location = selectedLocation  // new shelf location
                    )
                    productsRepository.insertProduct(newProduct)
                    println("Update Function: Move Quantity: $moveQuantity")
                }
                println("Update Function: Product Details on Putaway Screen: $productDetails")
                println("Update Function: Selected Location: $selectedLocation")
                println("Update Function: Product List: $productList")
                println("Update Function: Selected Product ID: $selectedProductId")
                println("Update Function: Original Location: $originalLocation")
                println("Update Function: Original Qty in Null Location: $originalQtyInNullLocation")
                println("Update Function: Original Quantity in Shelf: $originalQuantityInShelf")

                println("Update Function: Remaining Quantity: $remainingQuantity")

            }
        }
    }

//    companion object {
//        private const val TIMEOUT_MILLIS = 5_000L
//    }


}
