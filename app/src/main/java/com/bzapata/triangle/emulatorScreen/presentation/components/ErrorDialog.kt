package com.bzapata.triangle.emulatorScreen.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog() { // todo actually add error implementation
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {  },
            ) {
                Text(text = "Skip")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {  }
            ) {
                Text(text = "Cancel")
            }
        },
        text = {
            Column {
                Text(text = "Skip granting permissions?")
                Text(text = "Permission are used for DS Emulation")
            }
        },
        title = { Text(text = "Warning") },
    )
}