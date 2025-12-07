package com.bzapata.triangle.intro.skipPermissionDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bzapata.triangle.ui.theme.TriangleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkipPermissionDialog(onDismiss : () -> Unit, skip : () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {},
            ){
                Text(text = "Skip")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(text = "Cancel")
            }
        },
        text = {
            Column() {
                Text(text = "Skip granting permissions?")
                Text(text = "Permission are used for DS Emulation")
            }
        },
        title = { Text(text = "Warning") },
    )
}

@Preview
@Composable
fun SkipPermissionDialogPreview() {
    TriangleTheme {
        SkipPermissionDialog({},{})
    }
}