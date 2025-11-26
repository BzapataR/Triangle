// PagerIndicator.kt
// created by Brian Zapata Resendiz 11/20/2025
// Triangle
//
// Page indicator as dots for bottom app bar
package com.bzapata.triangle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.ui.theme.TriangleTheme


@Composable
fun PagerIndicator(
    pagerState: PagerState
) {
    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        BottomAppBar(
            modifier = Modifier.height(64.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
//        HorizontalPager(
//            state = pagerState,
//            modifier = Modifier.fillMaxSize()
//        ) { page ->
//        }
            Row(
                Modifier
                    //.wrapContentHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            )
            {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(6.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PagerIndicatorPreview() {
    TriangleTheme {
        val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 1)
        PagerIndicator(pagerState)
    }
}