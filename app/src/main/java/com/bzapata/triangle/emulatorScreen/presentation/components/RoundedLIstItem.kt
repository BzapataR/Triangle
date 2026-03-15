//
// Created By Brian Zapata Resendiz on 5/21/2025
// Trangle
// RoundedListItem.kt
// From another college project


package com.bzapata.triangle.emulatorScreen.presentation.components

import android.util.Log
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R

//********************************************************************************
//                    Function must be enclosed in a Card when
//                    Grouped with other RoundedList Items template
//                    below
//********************************************************************************
//Card(
//modifier = Modifier
//.padding(horizontal = 16.dp)
//.shadow(elevation = 24.dp),
//) { }
@Composable
fun RoundedListItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null, // optional onClick
    icon: ImageVector? = null, // optional Icon
    iconBackgroundColor: Color = Color.Unspecified, // Sets the Color of the circle around the icon
    iconColor: Color = MaterialTheme.colorScheme.background,
    leadingText: String,
    leadingTextColor: Color? = null,
    trailingText: String = "",
    trailingIcon: ImageVector? = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_right_24), // Icon at the end with default
    trailingIconColor: Color? = null,
    customTrailingContent: @Composable (() -> Unit)? = null, // used for anything, in this case added a switch
    iconModifier: Modifier = Modifier, // mainly used for icon size
) {
    // Manufacturer name was too long used to shrink
    fun shrinkText(string: String): String {
        return if (string.length >= 26) {
            string.take(10) + "..." // to customize the length of string before adding eclipse
        } else
            string
    }
    var isOverflowing by remember { mutableStateOf(false) }
    // edge fade from android documentation
    val edgeWidth = 32.dp
    fun ContentDrawScope.drawFadedEdge(leftEdge: Boolean) {
        val edgeWidthPx = edgeWidth.toPx()
        drawRect(
            topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
            size = Size(edgeWidthPx, size.height),
            brush =
                Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startX = if (leftEdge) 0f else size.width,
                    endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx,
                ),
            blendMode = BlendMode.DstIn,
        )
    }
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color(0xff2c2c2e)),
        leadingContent = if (icon == null) null else {
            {
                Box(
                    modifier = modifier
                        .size(24.dp) // Circle size
                        .background(
                            color = iconBackgroundColor,
                            shape = CircleShape // Circle shape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = modifier.size(20.dp)
                    )
                }
            }
        },
        headlineContent = {
            Box {
                // So why is this here? without this duplicate text everytime the text would move it would also re-trigger the onTextlayout check
                // thus making a loop and causing recompositions and making the text seem to flicker.
                // I already use this piece of code so much and I've been having problems with the edge affecting
                /// other lines of text that didn't overflow. So this was the more "modular" way of doing this.
                Text( // invisible text only used to measure if to turn on edge fading effect and the marquee effect.
                    text = leadingText,
                    maxLines = 1,
                    onTextLayout = { layout ->
                        if (isOverflowing != layout.hasVisualOverflow) {
                            isOverflowing = layout.hasVisualOverflow
                        }
                    },
                    modifier = Modifier.alpha(0f)
                )

                // VISIBLE TEXT: This gets the effects based on the ghost's measurement.
                Text(
                    text = leadingText,
                    maxLines = 1,
                    color = leadingTextColor ?: Color.Unspecified,
                    modifier = if (isOverflowing) {
                        Modifier
                            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                            .drawWithContent {
                                drawContent()
                                drawFadedEdge(leftEdge = true)
                                drawFadedEdge(leftEdge = false)
                            }
                            .basicMarquee(
                                iterations = Int.MAX_VALUE,
                                spacing = MarqueeSpacing(30.dp),
                                velocity = 125.dp
                            )
                    } else {
                        Modifier
                    }
                )
            }

        },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (customTrailingContent != null) {
                    customTrailingContent()
                } else {
                    Text(
                        text = shrinkText(trailingText),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .weight(12f, false)
                            .padding(end = 4.dp),
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,

                        )
                    if (trailingIcon != null) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null,
                            tint = trailingIconColor ?: Color.Gray,
                            modifier = iconModifier
                        )
                    }
                }
            }
        },
        modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
    )
}
