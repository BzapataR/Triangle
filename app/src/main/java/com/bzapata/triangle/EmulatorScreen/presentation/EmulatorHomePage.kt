//
// EmulatorHomePage.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/25/2025

package com.bzapata.triangle.emulatorScreen.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.emulatorScreen.domain.Consoles
import com.bzapata.triangle.emulatorScreen.presentation.emulators.components.GameGrid
import com.bzapata.triangle.ui.components.EmulatorAppBar
import com.bzapata.triangle.ui.components.PagerIndicator
import com.bzapata.triangle.settings.Settings
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun EmulatorHomePageRoot(viewModel: EmulatorViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EmulatorHomePage(
        state = state,
        onAction = {action ->
            viewModel.onAction(action)
        }
    )
}


@Composable
fun EmulatorHomePage(
    state: EmulatorState,
    onAction: (EmulatorActions) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { state.consoles.size }
    )
//    val consoles = Consoles.entries.toTypedArray()
//    val pagerState = rememberPagerState(
//        pageCount = {consoles.size}
//    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .blur(if (state.isBackgroundBlurred) 8.dp else 0.dp),
        topBar = {
            Column {
                EmulatorAppBar(
                    settingsToggle = { onAction(EmulatorActions.ToggleSettings) },
                    fileToggle = { onAction(EmulatorActions.ToggleFileContextMenu) },
                    isMenuOpen = state.isFileContextMenuOpen,
                    currentEmulatorName = state.consoles[pagerState.currentPage].name
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                PagerIndicator(pagerState = pagerState)
            }
        }
    ) { innerPadding ->

        HorizontalPager(
            modifier = Modifier.padding(innerPadding),
            state = pagerState
        ) { page ->
            GameGrid(
                console = state.consoles[page],
                onGameFocus = { onAction(EmulatorActions.ToggleGameContextMenu) }
                //focusedGame = -78, onGameFocus = {focusedGame}
            )
            // todo GameGrid(emulator type)
        }
    }
//                    GameGrid(
//                        modifier = Modifier.padding(innerPadding),
//                        focusedGame = selectedGameIndex,
//                        onGameFocus = { gameViewModel.onGameFocus(it) },
//                        console = Consoles.DS
//                    )
    Settings(dismissAction = { onAction(EmulatorActions.ToggleSettings) }, isOpen = state.isSettingsOpen)
}

@Preview
@Composable
fun EmulatorHomePagePreview() {
    TriangleTheme {
        val consoles = Consoles.entries.toTypedArray()
        val pagerState = rememberPagerState(pageCount = { consoles.size })
        EmulatorHomePage(EmulatorState(), onAction = {})
    }
}