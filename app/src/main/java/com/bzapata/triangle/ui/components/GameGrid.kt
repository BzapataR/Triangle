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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bzapata.triangle.Consoles
import com.bzapata.triangle.Game
import com.bzapata.triangle.R
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun GameGrid(
    console : Consoles,
    modifier: Modifier = Modifier,
//    focusedGame: Int?,
    onGameFocus: () -> Unit
) {
    var focusedGame by remember { mutableStateOf<Int?>(null) }
    //TODO remove the condition only to get different pages of games
    val game = when (console) {
        Consoles.NES -> {
            Game(name = "Super Maio Bros", cover = painterResource(R.drawable.super_mario_bros))
        }
        Consoles.SNES -> {
            Game(name = "Kirby Super Star", cover = painterResource(R.drawable.kirby_superstar))
        }
        Consoles.N64 -> {
            Game(name = "Super Smash Bros", cover = painterResource(R.drawable.super_smash_bros))
        }
        Consoles.GB -> {
            Game(name = "Mole Mania", cover = painterResource(R.drawable.mole_mania))
        }
        Consoles.GBA -> {
            Game(name = "Fire Emblem: Blazing Blade", cover = painterResource(R.drawable.fire_emblem_high))
        }
        Consoles.DS -> {
            Game(name = "Pokemon: Soulsilver", cover = painterResource(R.drawable.pokemon_soulsilver))
        }
    }
    val testImage = List(20) { it }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(90.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        //userScrollEnabled = focusedGame == null
    )
    {
        itemsIndexed(testImage) { index, _ ->
            Column {
                GameCover(
                    game = game,
                    isContextMenuShown = focusedGame == index,
                    onShowContextMenu = {
                        focusedGame = index
                        onGameFocus()
                    },
                    onDismissContextMenu = {
                        focusedGame = null
                        onGameFocus()
                    }
                )
                Text(
                    text = game.name,//todo get actual name
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
        GameGrid(console = Consoles.GBA, Modifier, {})
    }
}