//
// EmulatorPages.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/25/2025

package com.bzapata.triangle.ui.screens.Emulators

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bzapata.triangle.data.model.Consoles
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun EmulatorPages(
    blurBackgroundToggle : () -> Unit,
    modifier : Modifier,
    pagerState: PagerState,
    consoles: Array<Consoles>

) {
//    val consoles = Consoles.entries.toTypedArray()
//    val pagerState = rememberPagerState(
//        pageCount = {consoles.size}
//    )
    HorizontalPager(
        modifier = modifier,
        state = pagerState
    ) { page ->
        GameGrid(
            console = consoles[page],
            onGameFocus = { blurBackgroundToggle() }
            //focusedGame = -78, onGameFocus = {focusedGame}
        )
        // todo GameGrid(emulator type)
    }
}

@Preview
@Composable
fun EmulatorPagesPreview() {
    TriangleTheme {
        val consoles = Consoles.entries.toTypedArray()
        val pagerState = rememberPagerState(pageCount = { consoles.size })
        EmulatorPages({}, Modifier, pagerState, consoles)
    }
}