//
// Created By Brian Zapata Resendiz on 5/21/2025
// Trangle
// RoundedListItem.kt
// From another college project


package com.bzapata.triangle.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    iconColor : Color = MaterialTheme.colorScheme.background,
    leadingText: String,
    trailingText: String = "",
    trailingIcon: ImageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_right_24), // Icon at the end with default
    customTrailingContent : @Composable ( () -> Unit)? = null, // used for anything, in this case added a switch
    iconModifier: Modifier = Modifier, // mainly used for icon size
) {
    // Manufacturer name was too long used to shrink
    fun shrinkText(string: String) : String {
        return if(string.length >= 26) {
            string.take(10) + "..." // to customize the length of string before adding eclipse
        } else
            string
    }
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color(0xff2c2c2e)),
        leadingContent = if (icon ==null) null else{ {
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
                Text(
                    text = leadingText,
                    maxLines = 1,
                   // modifier = Modifier.weight(1f)
                )
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
                        modifier = Modifier.weight(12f,false).padding(end = 4.dp),
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,

                    )
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = iconModifier
                    )
                }
            }
        },
        modifier = if (onClick != null) Modifier.clickable{ onClick() } else Modifier
    )
}
