package com.bzapata.triangle.settings.controllerSettings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R
import com.bzapata.triangle.emulatorScreen.presentation.components.RoundedListItem
import com.bzapata.triangle.settings.SubText
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun ControllerSettingsRoot() {

}

@Composable
fun ControllerSettings(goBack: () -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xff1c1c1e))
                .padding(vertical = 8.dp),
        ) {
            Text(
                text = "Player 1",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
            TextButton(
                onClick = { goBack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_left_24),
                    contentDescription = null,
                )
                Text(
                    text = "Settings",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        SubText("ThisDevice")
        Card {
            RoundedListItem(
                leadingText = "Touch Screen",
                trailingIcon = ImageVector.vectorResource(R.drawable.baseline_check_24),
                trailingIconColor = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.size(24.dp))

        SubText("Game Controller")
        Card {
            RoundedListItem(
                leadingText = "No Controller Detected"
            )
        }
    }
}


@Preview
@Composable
fun ControllerSettingsPreview() {
    TriangleTheme {
        ControllerSettings({})
    }
}