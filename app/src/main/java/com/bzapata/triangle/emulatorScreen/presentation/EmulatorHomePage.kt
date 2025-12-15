//
// EmulatorHomePage.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/25/2025

package com.bzapata.triangle.emulatorScreen.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.emulatorScreen.data.fileOperations.directoryPicker
import com.bzapata.triangle.emulatorScreen.domain.Consoles
import com.bzapata.triangle.emulatorScreen.presentation.components.EmulatorAppBar
import com.bzapata.triangle.emulatorScreen.presentation.components.PagerIndicator
import com.bzapata.triangle.emulatorScreen.presentation.emulators.components.GameGrid
import com.bzapata.triangle.settings.SettingsNavigator
import com.bzapata.triangle.ui.theme.TriangleTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@Composable
fun EmulatorHomePageRoot() {
    val viewModel: EmulatorViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    EmulatorHomePage(
        state = state,
        onAction = viewModel::onAction
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmulatorHomePage(
    state: EmulatorState,
    onAction: (EmulatorActions) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { state.consoles.size }
    )
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                onAction(EmulatorActions.OnPageChange(page))
            }
    }

    val userDirectoryPicker = directoryPicker { uri ->
        onAction(EmulatorActions.SetUserFolder(uri))
        //todo fix crash on changeing folder on a page that it gets rid of
    }

    val romsDirectoryPicker = directoryPicker { uri ->
        onAction(EmulatorActions.SetRomsFolder(uri))
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .blur(if (state.isBackgroundBlurred) 8.dp else 0.dp),
        topBar = {
            EmulatorAppBar(
                settingsToggle = { onAction(EmulatorActions.ToggleSettings) },
                fileToggle = { onAction(EmulatorActions.ToggleFileContextMenu) },
                isMenuOpen = state.isFileContextMenuOpen,
                currentEmulatorName = if (state.consoles.isNotEmpty()) state.consoles[pagerState.currentPage].name else "",
                onChangeUserFolder = userDirectoryPicker,
                onChangeRomsFolder = romsDirectoryPicker
            )


        },
        bottomBar = {
            PagerIndicator(pagerState = pagerState)
        }
    ) { innerPadding ->
        if (state.consoles.isNotEmpty()) { // Add a check to prevent crashing
            HorizontalPager(
                modifier = Modifier.padding(innerPadding),
                state = pagerState,
            ) { page ->
                GameGrid(
                    games = state.games.filter { it.consoles == state.consoles[page] },
                    onGameFocus = { gameHash ->
                        onAction(EmulatorActions.ToggleGameContextMenu(gameHash))
                    },
                    state = state,
                )
            }
        }
    }
    SettingsNavigator(
        dismissAction = { onAction(EmulatorActions.ToggleSettings) },
        isOpen = state.isSettingsOpen
    )
}

@Preview
@Composable
fun EmulatorHomePagePreview() {
    TriangleTheme {
        val consoles = Consoles.entries.toTypedArray()
        rememberPagerState(pageCount = { consoles.size })
        EmulatorHomePage(EmulatorState(), onAction = {})
    }
}
