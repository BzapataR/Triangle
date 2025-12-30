//
// EmulatorHomePage.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/25/2025

package com.bzapata.triangle.emulatorScreen.presentation

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.emulatorScreen.domain.Consoles
import com.bzapata.triangle.emulatorScreen.domain.GameUiExample
import com.bzapata.triangle.emulatorScreen.presentation.components.AppBar
import com.bzapata.triangle.emulatorScreen.presentation.components.DatabaseCoverSelector
import com.bzapata.triangle.emulatorScreen.presentation.components.ErrorDialog
import com.bzapata.triangle.emulatorScreen.presentation.components.PagerIndicator
import com.bzapata.triangle.emulatorScreen.presentation.components.RenameDialog
import com.bzapata.triangle.emulatorScreen.presentation.components.SelectCoverActionSheet
import com.bzapata.triangle.util.fileLaunchers.directoryPicker
import com.bzapata.triangle.emulatorScreen.presentation.emulators.components.GameGrid
import com.bzapata.triangle.settings.SettingsNavigator
import com.bzapata.triangle.ui.theme.TriangleTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun EmulatorHomePageRoot() {
    val viewModel: EmulatorViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val activity = LocalActivity.current!!
    val windowSize = calculateWindowSizeClass(activity)
    LaunchedEffect(windowSize.widthSizeClass) {
        viewModel.changeWindowSize(windowSize.widthSizeClass)
    }
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
    val focusRequester = remember { FocusRequester() }
    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { state.consoles.size }
    )

    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        if (state.games.isNotEmpty()) {
            focusRequester.requestFocus()
        }
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
            //.background(Color.Transparent)
            .blur(if (state.isBackgroundBlurred) 8.dp else 0.dp),
        topBar = {
            AppBar(
                settingsToggle = { onAction(EmulatorActions.ToggleSettings) },
                fileToggle = { onAction(EmulatorActions.ToggleFileContextMenu) },
                windowWidth = state.windowSize,
                isMenuOpen = state.isFileContextMenuOpen,
                currentEmulatorName = if (state.romQuery.isNotEmpty()) "Results" else
                    state.consoles.getOrNull(state.currentPage)?.name ?: "",
                onChangeUserFolder = userDirectoryPicker,
                onChangeRomsFolder = romsDirectoryPicker,
                onQuery = {onAction(EmulatorActions.QuerySavedRoms(it))}
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
                    when {
                        state.romQuery.isEmpty() -> {
                            HorizontalPager(
                                modifier = Modifier.padding(innerPadding),
                                beyondViewportPageCount = 1,
                                state = pagerState,
                            ) { page ->
                                GameGrid(
                                    games = state.games.filter { it.consoles == state.consoles[page] },
                                    state = state,
                                    onAction = onAction,
                                    modifier = Modifier.focusRequester(focusRequester)
                                )
                            }
                        }
                        else -> {
                            GameGrid(
                                games = state.queriedRoms,
                                state = state,
                                onAction = onAction,
                                modifier = Modifier.padding(innerPadding).focusRequester(focusRequester)
                            )
                        }
                    }
                }
            }

            state.noRomPath -> NoRomPathMessage()
            state.isScanning -> ScanIndicator()
            state.games.isEmpty() && state.isInitialScanDone -> NoGamesFoundMessage()
        }
    }
    SettingsNavigator(
        dismissAction = { onAction(EmulatorActions.ToggleSettings) },
        isOpen = state.isSettingsOpen
    )
    SelectCoverActionSheet(state = state, onAction = onAction)
    DatabaseCoverSelector(state = state, onAction = onAction, game = state.selectedGame ?: return)
    if (state.errorMessage != null) {
        ErrorDialog(
            errorMessage = state.errorMessage,
            onDismiss = { onAction(EmulatorActions.ClearError) })
    }
    if (state.renameDialogOpen) {
        RenameDialog(
            game = state.selectedGame,
            onDismiss = { onAction(EmulatorActions.ToggleRenameDialog) },
            onConfirm = { newName ->
                onAction(EmulatorActions.RenameRom(newName))
            }
        )
    }

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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ScanIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingIndicator()
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
        EmulatorHomePage(EmulatorState(games = listOf(GameUiExample)), onAction = {})
    }
}
