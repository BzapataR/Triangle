//
// GameCover=[
// ].kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/21/2025
//
package com.bzapata.triangle.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.Game
import com.bzapata.triangle.R

@Composable
fun GameCover(
    game: Game,
    isContextMenuShown: Boolean,
    onShowContextMenu: () -> Unit,
    onDismissContextMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = {/*Launch Game*/},
                onLongClick = onShowContextMenu, // Original Delta open the game on this event but i don't like that instead focus on cover and show menu. wow this is a long comment should i split it or not? Nah no is looking at this... yet ;)
                onDoubleClick = {}, // Mayhaps we launch lastest quick save. // who is we?
                onClickLabel = "Launch ROM",
                onLongClickLabel = "Show ROM context menu",
            )
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,

        )
    {
        Image(
            painter = game.cover, // todo get cover from sqlite db by rom ID which is gotten from rom hash
            contentDescription = "Cover",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        GameContextMenu(
            expanded = isContextMenuShown,
            onDismissRequest = onDismissContextMenu
        )
    }
}