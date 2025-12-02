package com.bzapata.triangle.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController

@Composable
fun IntroNavigator() {
    val introNavigator = rememberNavController()
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        NavHost(
            navController = introNavigator,
            startDestination = IntroNavigation.IntroNavigationGraph
        ) {
            navigation<IntroNavigation.IntroNavigationGraph>(
                startDestination = IntroNavigation.Welcome
            ) {
                composable<IntroNavigation.Welcome> {

                }
            }
        }
    }
}