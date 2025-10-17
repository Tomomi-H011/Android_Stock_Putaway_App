package com.example.stockputawayapp.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockputawayapp.R
import com.example.stockputawayapp.StockPutawayTopAppBar
import com.example.stockputawayapp.data.Department
import com.example.stockputawayapp.navigation.NavigationDestination
import com.example.stockputawayapp.ui.AppViewModelProvider
import com.example.stockputawayapp.ui.theme.StockPutawayAppTheme


object SettingsScreenDestination : NavigationDestination {

    override val route = "settings_screen"

    override val titleRes = R.string.settings

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            StockPutawayTopAppBar(
                title = stringResource(SettingsScreenDestination.titleRes),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick,
                onInfoClick = onInfoClick
            )
        },
    ) { innerPadding ->

        SettingsBody(
            uiState = uiState,
            onDepartmentSelect = {viewModel.selectDepartment(it)},
            contentPadding = innerPadding,
            modifier = modifier
        )
    }
}

@Composable
fun SettingsBody(
    uiState: SettingsUiState,
    onDepartmentSelect: (Department) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
){
//    val viewModel: SettingsViewModel = viewModel()

    Column(
        modifier = modifier
            .padding(contentPadding)
            .padding(horizontal = 20
                .dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.choose_department),
            style = MaterialTheme.typography.bodyLarge
        )
       DepartmentSelector(
           departments = Department.values().toList(),
           selected = uiState.selectedDepartment,
           onSelect = onDepartmentSelect
       )
    }
}

@Composable
fun DepartmentSelector(
    departments: List<Department>,
    selected: Department?,
    onSelect: (Department) -> Unit
) {

    Column {

        // Display radio buttons for department selection
        departments.forEach { department ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onSelect(department) }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = department == selected,
                    onClick = { onSelect(department) }
                )
                Text(
                    text = department.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsBodyPreview() {
    val dummyUiState = SettingsUiState(selectedDepartment = Department.ELECTRONICS)
    StockPutawayAppTheme {
        SettingsBody(
            uiState = dummyUiState,
            onDepartmentSelect = {},

        )
    }
}