package com.example.stockputawayapp.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stockputawayapp.R
import com.example.stockputawayapp.StockPutawayTopAppBar
import com.example.stockputawayapp.navigation.NavigationDestination
import com.example.stockputawayapp.ui.theme.StockPutawayAppTheme


object InfoScreenDestination : NavigationDestination {

    override val route = "info_screen"

    override val titleRes = R.string.info

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StockPutawayTopAppBar(
                title = stringResource(InfoScreenDestination.titleRes),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick,
//                onInfoClick = { /* already on info screen */ }
            )
        },
    ) { innerPadding ->
        InfoBody(
            contentPadding = innerPadding,
            modifier = modifier
        )
    }
}

@Composable
fun InfoBody(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
){
    Column(
        modifier = modifier
            .padding(contentPadding)
            .padding(horizontal = 20
                .dp, vertical = 12.dp)
    ) {
        Text(
            text = "About the app:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = "This app lets users put away products on store shelves and record their locations.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Text(
            text = "How to use:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = "1. Click the Preference button, select the department you want to put away*. Go back to the Home screen.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "*This action will limit the products you see on the Home screen.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Text(
            text = "2. On the Home screen, select a product to put away. This will open the Putaway screen.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Text(
            text = "3. On the Putaway screen, enter the quantity and location to put away. Click Confirm.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 20.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun InfoScreenPreview() {
    StockPutawayAppTheme {
        InfoScreen(
            onBackClick = { /* no-op */ },
            onSettingsClick = { /* no-op */ }

        )
    }
}