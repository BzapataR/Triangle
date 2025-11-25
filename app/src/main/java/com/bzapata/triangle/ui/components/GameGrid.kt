//
// GameGrid.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/21/2025
//
package com.bzapata.triangle.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun GameGrid(
    modifier: Modifier = Modifier,
    focusedGame: Int?,
    onGameFocus: (Int?) -> Unit
) {
    val testImage = List(20) { it }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(90.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = focusedGame == null
    )
    {
        itemsIndexed(testImage) { index, _ ->
            Column {
                GameCover(
                    isContextMenuShown = focusedGame == index,
                    onShowContextMenu = { onGameFocus(index) },
                    onDismissContextMenu = { onGameFocus(null) }
                )
                Text(
                    text = "Fire Emblem: The Blazing Blade",//todo get actual name
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameGridPreview() {
    TriangleTheme {
        GameGrid(focusedGame = 0, onGameFocus = {})
    }
}