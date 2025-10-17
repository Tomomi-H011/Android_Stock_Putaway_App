package com.example.stockputawayapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockputawayapp.R
import com.example.stockputawayapp.StockPutawayTopAppBar
import com.example.stockputawayapp.data.Product
import com.example.stockputawayapp.navigation.NavigationDestination
import com.example.stockputawayapp.ui.AppViewModelProvider
import com.example.stockputawayapp.ui.theme.StockPutawayAppTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.select_product
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val uiState by viewModel.uiState.collectAsState()
    val productList = uiState.productList
    val selectedDepartment: String = uiState.selectedDepartment?.displayName ?: ""


    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StockPutawayTopAppBar(
                title = stringResource(com.example.stockputawayapp.ui.home.HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onBackClick = { /*No back for home screen*/ },
                onSettingsClick = onSettingsClick,
                onInfoClick = onInfoClick
            )
        },
    ) { innerPadding ->
        HomeBody(
            selectedDepartment = selectedDepartment,
            productList = productList,
            onProductClick = onProductClick,
            contentPadding = innerPadding,
            modifier = modifier
        )
    }
}

@Composable
private fun HomeBody(
    selectedDepartment: String,
    productList: List<Product>,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(contentPadding)
            .padding(top = 5.dp),
    ){
        Text(
            text = selectedDepartment.ifEmpty { "All Departments" },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp),
            textAlign = TextAlign.Left
        )
        if(productList.isEmpty()){
            Text(
                text = "No products to put away.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            ProductsList(
                productList = productList,
                onProductClick = { onProductClick(it.productId) },
                modifier = modifier
            )
        }
    }
}

// Display a list of products
@Composable
fun ProductsList(
    productList: List<Product>,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = productList, key = { it.id}) { product ->
            ProductItem(
                productName = product.productName ?: "",
                productId = product.productId,
                lotNumber = product.lotNumber ?: "",
                quantity = product.quantity ?: 0,

                modifier = Modifier.clickable { onProductClick(product) }
            )
        }
    }
}



// Display a card of product information
@Composable
private fun ProductItem(
    productName: String,
    productId: String,
    lotNumber: String,
    quantity: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(12.dp),
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
                    modifier = Modifier.width(110.dp)

                )
                Text(
                    text = productName,
                    style = MaterialTheme.typography.bodyLarge,


                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 5.dp)
            ) {
                Column {
                    Row {
                        Text(
                            text = "Prod No: ",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.width(110.dp)
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
                            modifier = Modifier.width(110.dp)
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


@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    StockPutawayAppTheme {
        HomeBody(
            productList = listOf(
                Product(
                    productName = "Toothpaste",
                    productId = "P30003",
                    lotNumber = "30000002",
                    department = "Health",
                    quantity = 10,
                    location = null
                ),
                Product(
                    productName = "Mouse - Wireless",
                    productId = "P30223",
                    lotNumber = "30000011",
                    department = "Electronic",
                    quantity = 20,
                    location = null
                ),
                Product(
                    productName = "Monitor - 32 inch",
                    productId = "P30007",
                    lotNumber = "30000202",
                    department = "Electronic",
                    quantity = 60,
                    location = null
                )
        ),
            selectedDepartment = "DEPT NAME",
            onProductClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    StockPutawayAppTheme {
        HomeBody(
            selectedDepartment = "DEPT NAME",
            listOf(),
            onProductClick = {})
    }
}
//@Preview(showBackground = true)
//@Composable
//fun ProductItemPreview() {
//    StockPutawayAppTheme {
//            ProductItem(
//                productName = "Toothpaste",
//                productId = "P30003",
//                lotNumber = "30000002",
//                quantity = 10
//            )
//
//    }
//}