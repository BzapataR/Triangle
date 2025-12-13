package com.bzapata.triangle.emulatorScreen.presentation.emulators.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.Coil
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.bzapata.triangle.emulatorScreen.domain.Game
import com.bzapata.triangle.emulatorScreen.domain.GameUiExample

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameCover(
    game: Game,
    isContextMenuShown: Boolean,
    onShowContextMenu: () -> Unit,
    onDismissContextMenu: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Box(
            modifier = Modifier
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {/*Launch Game*/ },
                    onLongClick = { onShowContextMenu() }, // Original Delta open the game on this event but i don'''t like that instead focus on cover and show menu. wow this is a long comment should i split it or not? Nah no is looking at this... yet ;)
                    onDoubleClick = {}, // Mayhaps we launch lastest quick save. // who is we?
                    onClickLabel = "Launch ROM",
                    onLongClickLabel = "Show ROM context menu",
                )
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 2.dp,
                    color = if (isHovered) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,

            )
        {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(game.cover) // Pass the Uri object here
                        .crossfade(true)
                        .build(),
                    contentDescription = "Cover",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                    // You can also define placeholder/error content within the AsyncImage scope

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
