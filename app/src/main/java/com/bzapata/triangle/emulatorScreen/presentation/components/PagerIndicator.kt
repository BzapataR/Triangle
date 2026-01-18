// PagerIndicator.kt
// created by Brian Zapata Resendiz 11/20/2025
// Triangle
//
// Page indicator as dots for bottom app bar
package com.bzapata.triangle.emulatorScreen.presentation.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R
import com.bzapata.triangle.data.controller.ControllerManager
import com.bzapata.triangle.ui.theme.TriangleTheme
import com.bzapata.triangle.util.controllerIcons.leftTriggerIcons
import com.bzapata.triangle.util.controllerIcons.rightTriggerIcons


@Composable
fun PagerIndicator(
    pagerState: PagerState,
    controllerType : ControllerManager.ControllerType?
) {
    Column(modifier = Modifier.focusProperties { canFocus = false }) {
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
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = if (controllerType != null)Arrangement.SpaceBetween else Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                if (controllerType != null) {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_double_arrow_left_24),
                            contentDescription = null
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(leftTriggerIcons(controllerType)),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Row {
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
                if (controllerType != null) {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(rightTriggerIcons(controllerType)),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_double_arrow_right_24),
                            contentDescription = null
                        )
                    }
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
        PagerIndicator(pagerState, null)
    }
}