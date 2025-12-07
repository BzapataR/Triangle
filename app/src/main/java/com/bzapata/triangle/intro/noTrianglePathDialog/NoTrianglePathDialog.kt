package com.bzapata.triangle.intro.noTrianglePathDialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun NoTrianglePathDialog(dismiss : () -> Unit ) {
    AlertDialog(
        onDismissRequest = { dismiss() },
        confirmButton = { TextButton(onClick = { dismiss() }) {
            Text(text = "Close")
        } },
        title = {
            Text(text = "You can't skip settings up the user folder")
        },
        text = {
            Text(text = "This step is required to allow Triangle to work. Please select a " +
                    "directory and then you can continue")
        }
    )
}

@Preview
@Composable
fun NoTrianglePathDialogPreview() {
    TriangleTheme {
        NoTrianglePathDialog({})
    }
}