package com.bzapata.triangle.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsNavigator(
    sheetState: SheetState
) {
    val settingsNavigation = rememberNavController()
    NavHost(
        navController = settingsNavigation,
        startDestination = SettingsNavigation.SettingsNavigationGraph,
    ){
        navigation<SettingsNavigation.SettingsNavigationGraph>(
            startDestination = SettingsNavigation.SettingsMainPage
        ) {
            composable<SettingsNavigation.SettingsMainPage> {
                Settings(sheetState = sheetState)
            }
        }
    }
}