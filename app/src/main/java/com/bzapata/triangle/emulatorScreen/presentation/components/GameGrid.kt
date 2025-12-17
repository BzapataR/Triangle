//
// GameGrid.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/21/2025
//
package com.bzapata.triangle.emulatorScreen.presentation.emulators.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.emulatorScreen.domain.Game
import com.bzapata.triangle.emulatorScreen.domain.GameUiExample
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorState
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun GameGrid(
    games: List<Game>,
    onGameFocus: (String?) -> Unit,
    state: EmulatorState,
) {

    LaunchedEffect(state.gameHashForContextMenu) {
        Log.i("Page", "state hash changed to: ${state.gameHashForContextMenu}")
    }
    key(state.gameHashForContextMenu) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(90.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        )
        {
            items(items = games, key = { it.hash }) { game -> //todo fix one rom glitch
                val openContextMenu = state.gameHashForContextMenu == game.hash
                GameCover(
                    game = game,
                    isContextMenuShown = openContextMenu,
                    onShowContextMenu = {
                        onGameFocus(game.hash)
                        Log.i(
                            "Page",
                            "state hash: ${state.gameHashForContextMenu} and game hash ${game.hash}"
                        )
                    },
                    onDismissContextMenu = {
                        Log.i("Page", "dismiss context menu")
                        onGameFocus(null)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameGridPreview() {
    TriangleTheme {
        GameGrid(state = EmulatorState(), onGameFocus = { 0 }, games = listOf(GameUiExample))
    }
}