package com.bzapata.triangle.intro.Welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.bzapata.triangle.R
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun Welcome() {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column{
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.triangle_logo_foreground),
                contentDescription = "Ripoff"
            )
            Text(
                "Welcome!",
                style = MaterialTheme.typography.titleLarge
            )
        }
        Text(
            text = "First we need to set up the app"
        )
        Button(
            onClick = {}
        ) {
            Text("Get Started")
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_right_24),
                contentDescription = "Next Page"
            )
        }
    }
}

@Preview
@Composable
fun WelcomePreview() {
    TriangleTheme {
        Welcome()
    }
}