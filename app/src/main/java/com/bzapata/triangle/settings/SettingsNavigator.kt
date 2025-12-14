package com.bzapata.triangle.settings

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.bzapata.triangle.settings.controllerSettings.ControllerSettings
import com.bzapata.triangle.settings.controllerSkins.ControllerSkinsRoot
import com.bzapata.triangle.settings.mainSettingsPage.Settings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsNavigator(
    isOpen: Boolean,
    dismissAction: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val modalBottomSheetProperties = remember {
        ModalBottomSheetProperties(
            isAppearanceLightStatusBars = false,
            isAppearanceLightNavigationBars = false,
        )
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    if (isOpen) {
        ModalBottomSheet(
            modifier = Modifier
            //.fillMaxHeight(.9f)
            // .padding(top = 50.dp)
            ,
            dragHandle = { },
            onDismissRequest = { dismissAction() },
            sheetState = sheetState,
            containerColor = Color(0xff1c1c1e),
            sheetGesturesEnabled = false,
            properties = modalBottomSheetProperties

        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(.94f)
                    .padding(horizontal = 16.dp),
            ) {
                val settingsNavigation = rememberNavController()
                NavHost(
                    navController = settingsNavigation,
                    startDestination = SettingsNavigation.SettingsNavigationGraph,
                    enterTransition = { slideIntoContainer(Left, tween(500)) },
                    popEnterTransition = { slideIntoContainer(Right, tween(500)) },
                    exitTransition = { slideOutOfContainer(Left, tween(500)) },
                    popExitTransition = { slideOutOfContainer(Right, tween(500)) }
                ) {
                    navigation<SettingsNavigation.SettingsNavigationGraph>(
                        startDestination = SettingsNavigation.SettingsMainPage
                    ) {
                        composable<SettingsNavigation.SettingsMainPage> {
                            Settings(
                                dismiss = {
                                    scope.launch {
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible)
                                            dismissAction()
                                    }
                                },
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
                                if (settingsNavigation.previousBackStackEntry != null) {
                                    settingsNavigation.popBackStack()
                                }
                            })
                        }
                        composable<SettingsNavigation.ControllerSkins> {
                            ControllerSkinsRoot(goBack = {
                                if (settingsNavigation.previousBackStackEntry != null) {
                                    settingsNavigation.popBackStack()
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}
