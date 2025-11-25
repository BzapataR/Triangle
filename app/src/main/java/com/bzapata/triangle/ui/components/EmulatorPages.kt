//
// EmulatorPages.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/25/2025

package com.bzapata.triangle.ui.components

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun EmulatorPages() {
    val pagerState = rememberPagerState(
        pageCount = {10}
    )

    HorizontalPager(
        state = pagerState
    ) { page ->
        GameGrid(focusedGame = -78, onGameFocus = {})
        // todo GameGrid(emulator type)
    }
}

@Preview
@Composable
fun EmulatorPagesPreview() {
    TriangleTheme {
        EmulatorPages()
    }
}