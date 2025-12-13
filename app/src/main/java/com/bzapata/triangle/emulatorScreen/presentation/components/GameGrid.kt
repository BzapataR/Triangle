//
// GameGrid.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/21/2025
//
package com.bzapata.triangle.emulatorScreen.presentation.emulators.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.emulatorScreen.domain.Consoles
import com.bzapata.triangle.emulatorScreen.domain.GameUiExample
import com.bzapata.triangle.R
import com.bzapata.triangle.emulatorScreen.domain.Game
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorState
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun GameGrid(
    console: Consoles,
    games: List<Game>,
//    focusedGame: Int?,
    onGameFocus:(Int?) -> Unit,
    state: EmulatorState,
    pageNumber : Int
) {


    LazyVerticalGrid(
        columns = GridCells.Adaptive(90.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    )
    {
        itemsIndexed(games) { index, game->
                GameCover(
                    game = game,
                    isContextMenuShown = (state.gameIndexForContextMenu == index && pageNumber == state.currentPage),
                    onShowContextMenu = {
                        onGameFocus(index)
                    },
                    onDismissContextMenu = {
                        onGameFocus(null)
                    }
                )
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun GameGridPreview() {
//    TriangleTheme {
//        GameGrid(console = Consoles.GBA, state = EmulatorState(), onGameFocus = {0}, pageNumber= 1 )
//    }
//}