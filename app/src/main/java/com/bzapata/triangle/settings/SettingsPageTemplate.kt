package com.bzapata.triangle.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R

@Composable
fun SettingsPageTemplate(goBack : () -> Unit, title : String, modifier : Modifier = Modifier, content : LazyListScope.() -> Unit) {
    val listState = rememberLazyListState()
    val headerAlpha by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else (listState.firstVisibleItemScrollOffset / 120f).coerceIn(0f, 1f)
        }
    }

    LazyColumn(
        modifier = modifier.then(Modifier.fillMaxSize().padding(horizontal = 16.dp)),
        state = listState,
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xff1c1c1e))
                    .padding(vertical = 8.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = headerAlpha
                            translationY = (1f - headerAlpha) * -12f
                        }
                        .align(Alignment.Center)
                )

                TextButton(
                    onClick = { goBack() },
                    modifier = Modifier.align(Alignment.CenterStart),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_left_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Settings",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
            )
            Spacer(modifier = Modifier.size(24.dp))
        }
        content()
    }
}