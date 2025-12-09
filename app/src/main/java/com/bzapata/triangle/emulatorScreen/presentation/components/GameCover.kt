//
// GameCover.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/21/2025
//
package com.bzapata.triangle.emulatorScreen.presentation.emulators.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bzapata.triangle.emulatorScreen.domain.Game

@Composable
fun GameCover(
    game: Game,
    isContextMenuShown: Boolean,
    onShowContextMenu: () -> Unit,
    onDismissContextMenu: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Box(
            modifier = Modifier
                .combinedClickable(
                    onClick = {/*Launch Game*/ },
                    onLongClick = { onShowContextMenu() }, // Original Delta open the game on this event but i don'''t like that instead focus on cover and show menu. wow this is a long comment should i split it or not? Nah no is looking at this... yet ;)
                    onDoubleClick = {}, // Mayhaps we launch lastest quick save. // who is we?
                    onClickLabel = "Launch ROM",
                    onLongClickLabel = "Show ROM context menu",
                )
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,

            )
        {
            Image(
                painter = game.cover, // todo get cover from sqlite db by rom ID which is gotten from rom hash
                contentDescription = "Cover",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            GameContextMenu(
                gameName = game.name,
                expanded = isContextMenuShown,
                onDismissRequest = onDismissContextMenu
            )
        }
        Text(
            text = game.name,//todo get actual name
            textAlign = TextAlign.Center,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            color = Color.Gray
        )
    }
}