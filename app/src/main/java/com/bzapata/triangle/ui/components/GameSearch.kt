//
// GameSearch.kt
// Created by Brian Zapata Resendiz on 11/19/2025
// Triangle
//
// Search Barrrrrrr
package com.bzapata.triangle.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun GameSearch() {
    val textState = rememberTextFieldState()
    var isFocused by remember { mutableStateOf(false) }
    val focusManager: FocusManager = LocalFocusManager.current

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    //val screenWidth = LocalWindowInfo.current.containerSize.width.dp

    val animatedWidth by animateDpAsState(
        targetValue = if (isFocused) screenWidth - 90.dp else screenWidth - 32.dp,
        animationSpec = spring(
            dampingRatio = 0.85f,
            stiffness = 200f
        ),
        label = "Width Ani"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        TextField(
            state = textState,
            placeholder = {
                Text("Search")
            },
            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                top = 8.dp,
                bottom = 8.dp
            ),
            modifier = Modifier
                .width(animatedWidth)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.sharp_search_24),
                    contentDescription = "Look For Games"
                )
            },
            trailingIcon = {
                if (textState.text.isNotEmpty()) {
                    IconButton(
                        onClick = { textState.clearText() }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.sharp_close_24),
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//            keyboardActions = KeyboardActions(
//                onSearch = {
//                    keyboardController?.hide()
//                }
//            ),
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        AnimatedVisibility(
            visible = isFocused,
            enter = slideInHorizontally(
                animationSpec = spring(
                    dampingRatio = 0.85f,
                    stiffness = 200f
                )
            ) { it } + fadeIn(),
            exit = slideOutHorizontally(
                animationSpec = spring(
                    dampingRatio = 0.85f,
                    stiffness = 200f
                )
            ) { it } + fadeOut()
        ) {
            TextButton(
                onClick = {
                    focusManager.clearFocus()
                    isFocused = false
                    textState.clearText()
                },
                modifier = Modifier.padding(start = 8.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Cancel", maxLines = 1, softWrap = false)
            }
        }
    }
}
