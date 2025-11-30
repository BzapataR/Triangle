//
// Setting Sheet.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/22/2025
//
package com.bzapata.triangle.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.bzapata.triangle.ui.theme.TriangleTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Settings(
    isOpen: Boolean,
    dismissAction: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (isOpen) {
        ModalBottomSheet(
            modifier = Modifier
            //.fillMaxHeight(.9f)
            // .padding(top = 50.dp)
            ,
            dragHandle = { },
            onDismissRequest = { dismissAction() },
            sheetState = sheetState,
            containerColor = Color(0xff1c1c1e),
            sheetGesturesEnabled = false

        ) {
            Settings(sheetState)
        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
    TriangleTheme {
        Settings(true) {}
    }
}