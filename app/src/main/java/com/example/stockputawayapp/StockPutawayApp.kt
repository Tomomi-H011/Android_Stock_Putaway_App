@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.stockputawayapp

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.stockputawayapp.R.string
import com.example.stockputawayapp.navigation.StockPutawayNavHost

// Top level composable that represents screens for the application.
@Composable
fun StockPutawayApp(navController: NavHostController = rememberNavController()) {
    StockPutawayNavHost(navController = navController)
}

// App bar to display title and conditionally display the back navigation.
@Composable
fun StockPutawayTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onInfoClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = modifier
            )
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Filled.ArrowBack,
                        contentDescription = stringResource(string.back_button),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {

                Spacer(modifier = Modifier.width(48.dp))
            }
        },
        actions = {
            IconButton(onClick = onInfoClick) {
                Icon(
                    imageVector = Filled.Info,
                    contentDescription = stringResource(string.info),
                    modifier = modifier.padding(end = 15.dp)
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Filled.Settings,
                    contentDescription = stringResource(string.settings),
                    modifier = modifier.padding(end = 5.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,      // background
            titleContentColor = MaterialTheme.colorScheme.onPrimary,  // title text
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary // action icons
        )
    )
}


@Preview(showBackground = true)
@Composable
fun StockPutawayTopAppBarPreview() {
    MaterialTheme {
        Surface {
            StockPutawayTopAppBar(
                title = "Putaway",
                canNavigateBack = true,
                onBackClick = { /* no-op for preview */ }
            )
        }
    }
}