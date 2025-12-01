package com.bzapata.triangle.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.bzapata.triangle.settings.controllerSettings.ControllerSettings
import com.bzapata.triangle.settings.controllerSkins.ControllerSkinsRoot
import com.bzapata.triangle.settings.mainSettingsPage.Settings

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
                Settings(
                    sheetState = sheetState,
                    toControllerSettings = {
                        settingsNavigation.navigate(SettingsNavigation.ControllerSettings)
                    },
                    toControllerSkins = {
                        settingsNavigation.navigate(SettingsNavigation.ControllerSkins)
                    }
                )
            }
            composable<SettingsNavigation.ControllerSettings> {
                ControllerSettings(goBack = {
                    settingsNavigation.popBackStack()
                })
            }
            composable<SettingsNavigation.ControllerSkins> {
                ControllerSkinsRoot(goBack = {
                    settingsNavigation.popBackStack()
                })
            }
        }
    }
}