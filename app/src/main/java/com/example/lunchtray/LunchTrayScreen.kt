/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.OrderViewModel
import com.example.lunchtray.ui.SideDishMenuScreen
import com.example.lunchtray.ui.StartOrderScreen

// TODO: Screen enum
enum class LaunchTray(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Entree(title = R.string.choose_entree),
    SideDish(title = R.string.choose_side_dish),
    Accompaniment(title = R.string.choose_accompaniment),
    Checkout(title = R.string.order_checkout)
}

// TODO: AppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // TODO: Create Controller and initialization
    Scaffold(
//        topBar = {
//            // TODO: AppBar
//        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        // TODO: Navigation host
        NavHost(
            navController = navController,
            startDestination = LaunchTray.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = LaunchTray.Start.name) {
                StartOrderScreen(
                    onStartOrderButtonClicked = {
                        navController.navigate(LaunchTray.Entree.name)
                    }
                )
            }
            composable(route = LaunchTray.Entree.name) {
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onSelectionChanged = { viewModel.updateEntree(it) },
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(route = LaunchTray.Start.name, inclusive = false)
                    },
                    onNextButtonClicked = {
                        navController.navigate(LaunchTray.SideDish.name)
                    }
                )
            }
            composable(route = LaunchTray.SideDish.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onSelectionChanged = { viewModel.updateSideDish(it) },
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(route = LaunchTray.Start.name, inclusive = false)
                    },
                    onNextButtonClicked = {
                        navController.navigate(LaunchTray.Accompaniment.name)
                    }
                )
            }
            composable(route = LaunchTray.Accompaniment.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onSelectionChanged = { viewModel.updateAccompaniment(it) },
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(route = LaunchTray.Start.name, inclusive = false)
                    },
                    onNextButtonClicked = {
                        navController.navigate(LaunchTray.Checkout.name)
                    }
                )
            }
            composable(route = LaunchTray.Checkout.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onNextButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(route = LaunchTray.Start.name, inclusive = false)
                    },
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(route = LaunchTray.Start.name, inclusive = false)
                    }
                )
            }
        }
    }
}
