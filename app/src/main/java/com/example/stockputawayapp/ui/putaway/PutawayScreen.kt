package com.example.stockputawayapp.ui.putaway

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockputawayapp.R
import com.example.stockputawayapp.StockPutawayTopAppBar
import com.example.stockputawayapp.navigation.NavigationDestination
import com.example.stockputawayapp.ui.AppViewModelProvider
import com.example.stockputawayapp.ui.theme.StockPutawayAppTheme


object PutAwayDestination : NavigationDestination {

    override val route = "putaway"

    override val titleRes = R.string.putaway
    const val productIdArg = "productId"

    val routeWithArgs = "$route/{$productIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PutawayScreen(
    productId: String?,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PutawayViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
//    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()
    val productDetails = uiState.value.productDetails
//    println("Product Details when clicking Product: $productDetails")

    var showSuccessDialog by remember { mutableStateOf(false) }


    // Snapshot of original quantity (decoupled from ViewModel)
    val originalQuantity = remember(productDetails.productId) {
        productDetails.quantity ?: 0
    }

    // Load available locations for the department
    LaunchedEffect(productDetails.department) {
        val dept = productDetails.department
        if (!dept.isNullOrBlank()) {
            viewModel.loadLocations(dept)
        }
    }


    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StockPutawayTopAppBar(
                title = stringResource(com.example.stockputawayapp.ui.home.HomeDestination.titleRes),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick,
                onInfoClick = onInfoClick
            )
        },
    ) { innerPadding ->
        PutawayBody(
            productName = productDetails.productName ?: "",
            productId = productDetails.productId,
            lotNumber = productDetails.lotNumber ?: "",
            initialQuantity = productDetails.quantity ?: 0, // For the text field
            originalQuantity = originalQuantity, // Display-only, snapshot for ProductDetail section
            initialLocation = "",
            availableLocations = uiState.value.availableLocations,
            onConfirmClicked = {
                viewModel.updateQtyAndLoc()
                showSuccessDialog = true
            },
            onCancelClicked = onBackClick,
            onLocationSelected = {viewModel.selectLocation(it)},
            onQuantityChanged = { viewModel.updateQuantity(it) },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    onBackClick()  // navigate back to home screen
                }) {
                    Text(
                        "OK",
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            },
            title = { Text("Success") },
            text = { Text("Quantity and location updated successfully!") }
        )
    }
}




@Composable
fun PutawayBody(
    productName: String,
    productId: String,
    lotNumber: String,
    initialQuantity: Int,
    originalQuantity: Int,
    initialLocation: String?,
    availableLocations: List<String>,
    onConfirmClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    onLocationSelected: (String) -> Unit,
    onQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    // State to hold the currently selected location
    var selectedLocation by rememberSaveable { mutableStateOf(initialLocation ?: "") }

    // State to show an error dialog if location is empty
    var showLocationError by remember { mutableStateOf(false) }

    Column (
        modifier = modifier
//            .verticalScroll(rememberScrollState())
    ) {
        ProductDetail(
            productName = productName,
            productId = productId,
            lotNumber = lotNumber,
            quantity = originalQuantity  // show snapshot
        )
        ProductQuantityAndLocation(
            initialQuantity = initialQuantity,
            originalQuantity = originalQuantity,
            initialLocation = initialLocation,
            availableLocations = availableLocations,
            onLocationSelected = { loc ->  // update location on each selection
                selectedLocation = loc
                onLocationSelected(loc)
            },
            onQuantityChanged = onQuantityChanged
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val buttonWidth = 100.dp
            Button(
                onClick = {
                    if (selectedLocation.isBlank()) {
                        // Show error if location is empty
                        showLocationError = true
                    } else {
                        onConfirmClicked()
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .width(buttonWidth),
            ) {
                Text(stringResource(R.string.confirm))
            }
            OutlinedButton(
                onClick = onCancelClicked,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier
                    .padding(10.dp)
                    .width(buttonWidth),
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    }

    // Error dialog for missing location
    if (showLocationError) {
        AlertDialog(
            onDismissRequest = { showLocationError = false },
            confirmButton = {
                TextButton(onClick = { showLocationError = false }) {
                    Text(
                        "OK",
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            },
            title = { Text("Location Required") },
            text = { Text("Please select a location before confirming.") }
        )
    }
}


@Composable
private fun ProductDetail(
    productName: String,
    productId: String,
    lotNumber: String,
    quantity: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(top = 30.dp, start = 12.dp, end = 12.dp, bottom = 12.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Prod Name: ",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.width(115.dp)

                )
                Text(
                    text = productName,
                    style = MaterialTheme.typography.bodyLarge,


                    )
                }
            Row {
                Column {
                    Row {
                        Text(
                            text = "Prod No: ",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.width(115.dp)
                        )
                        Text(
                            text = productId,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 5.dp)
                    ) {
                        Text(
                            text = "Lot No: ",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.width(115.dp)
                        )
                        Text(
                            text = lotNumber,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(0.3f))

                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .width(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Qty",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductQuantityAndLocation(
    initialQuantity: Int,
    originalQuantity: Int,
    initialLocation: String?,
    availableLocations: List<String>,
    onLocationSelected: (String) -> Unit,
    onQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showError by remember { mutableStateOf(false) }

    var qty by rememberSaveable{mutableStateOf("")}
    LaunchedEffect(initialQuantity) {
        qty = initialQuantity.toString()
    }

    var location by rememberSaveable { mutableStateOf(initialLocation ?: "") }

    Row(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .width(120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Qty",
                style = MaterialTheme.typography.bodyMedium,
            )
            OutlinedTextField(
                value = qty,
                onValueChange = { newValue ->
                    when {
                        newValue.isEmpty() -> {
                            qty = ""
                            onQuantityChanged(0)

                        }
                        newValue.all { it.isDigit() } -> {
                            val number = newValue.toIntOrNull()
                            if (number != null && number in 1..originalQuantity) {
                                qty = newValue
                                onQuantityChanged(number)
//                                showError = false
                            } else {
                                showError = true
                            }
                        }
                        else -> {
                            showError = true
                        }
                    }
                },

                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                shape = RoundedCornerShape( 0.dp ),
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(Alignment.CenterHorizontally)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            qty = ""
                        }
                    }
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .width(120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Location",
                style = MaterialTheme.typography.bodyMedium,
            )
            LocationDropdown(
                locations = availableLocations, // passed from parent
                selectedLocation = location,
                onLocationSelected = { selected ->
                    location = selected
                    onLocationSelected(selected)
                },
                modifier = Modifier
                    .padding(top = 5.dp)
            )
        }
        // Show error message if invalid quantity
        if (showError) {
            AlertDialog(
                onDismissRequest = { showError = false },
                confirmButton = {
                    TextButton(onClick = { showError = false }) {
                        Text(
                            "OK",
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                title = { Text("Invalid Quantity") },
                text = { Text("Quantity must be between 1 and $originalQuantity.") }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDropdown(
    locations: List<String>,
    selectedLocation: String,
    onLocationSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedLocation,
            onValueChange = {},
            readOnly = true, // Disable editing
//            label = { Text("Location") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor(),
            shape = RoundedCornerShape( 0.dp )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(100.dp),

        ) {
            locations.forEach { location ->
                DropdownMenuItem(
                    text = { Text(location) },
                    onClick = {
                        onLocationSelected(location)
                        expanded = false
                    }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ProductDetailPreview() {
    StockPutawayAppTheme {
        PutawayBody(
            productName = "Toothpaste",
            productId = "P30003",
            lotNumber = "30000002",
            initialQuantity = 10,
            originalQuantity = 10,
            initialLocation = "A06",
            onConfirmClicked = {},
            onCancelClicked= {},
            onLocationSelected = {},
            onQuantityChanged = {},
            availableLocations = listOf("A01", "A02", "A03", "A04", "A05", "A06", "A07", "A08", "A09")

        )
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun ProductQuantityAndLocationPreview() {
//    StockPutawayAppTheme {
//        ProductQuantityAndLocation(
////            productName = "Toothpaste",
////            productId = "P30003",
////            lotNumber = "30000002",
//            initialQuantity = 10,
//            initialLocation = "A06"
//        )
//
//    }
//}