//
// EmulatorHomePage.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/25/2025

package com.bzapata.triangle.emulatorScreen.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.style.TextAlign
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

    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(state.consoles) {
            pagerState.scrollToPage(0)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                onAction(EmulatorActions.OnPageChange(page))
            }
    }

    val userDirectoryPicker = directoryPicker { uri ->
        onAction(EmulatorActions.SetUserFolder(uri))
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
                currentEmulatorName = state.consoles.getOrNull(state.currentPage)?.name ?: "",
                onChangeUserFolder = userDirectoryPicker,
                onChangeRomsFolder = romsDirectoryPicker
            )
        },
        bottomBar = {
            if (state.consoles.isNotEmpty()) {
                PagerIndicator(pagerState = pagerState)
            }
        }
    ) { innerPadding ->
        when {
            state.consoles.isNotEmpty() -> {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { onAction(EmulatorActions.RefreshRomList) },
                    state = pullToRefreshState,
                    indicator = {
                        Indicator(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(innerPadding),
                            isRefreshing = state.isRefreshing,
                            containerColor = MaterialTheme.colorScheme.surface,
                            color = MaterialTheme.colorScheme.primary,
                            state = pullToRefreshState
                        )
                    }
                ) {
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
            state.noRomPath -> NoRomPathMessage()
            state.isScanning && state.isInitialScanDone -> ScanIndicator()
            state.games.isEmpty() && state.isInitialScanDone -> NoGamesFoundMessage()
        }
    }
    SettingsNavigator(
        dismissAction = { onAction(EmulatorActions.ToggleSettings) },
        isOpen = state.isSettingsOpen
    )
}

@Composable
private fun NoRomPathMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No Roms folder detected!")
        Text("Add Roms folder by tapping the \"+\" icon", textAlign = TextAlign.Center)
    }
}

@Composable
private fun ScanIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.size(8.dp))
        Text("Scanning Roms")
    }
}

@Composable
private fun NoGamesFoundMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Log.i("loading", "No games were found after scan.")
        Text("No Roms Found")
    }
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
