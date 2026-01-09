//
// GameGrid.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/21/2025
//
package com.bzapata.triangle.emulatorScreen.presentation.emulators.components

import android.util.Log
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
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
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorActions
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorState
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun GameGrid(
    modifier: Modifier = Modifier,
    games: List<Game>,
    state: EmulatorState,
    onAction: (EmulatorActions) -> Unit
) {

//    LaunchedEffect(state.gameHashForContextMenu) {
//        Log.i("Page", "state hash changed to: ${state.gameHashForContextMenu}")
//    }
//    key(state.gameHashForContextMenu) { //When 1 rom is loaded context menu won't open without this
        LazyVerticalGrid(
            columns = GridCells.Adaptive(90.dp),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        )
        {
            items(items = games, key = { it.hash }) { game -> //todo fix one rom glitch
                GameCover(
                    game = game,
                    isContextMenuShown = state.gameHashForContextMenu == game.hash,
                    onActions = onAction,
                    state = state,
                )
            }
        }
//    }
}

@Preview(showBackground = true)
@Composable
fun GameGridPreview() {
    TriangleTheme {
        GameGrid(state = EmulatorState(), games = listOf(GameUiExample), onAction = {})
    }
}