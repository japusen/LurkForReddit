package com.example.lurkforreddit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MoreCommentsIndicator(
    depth: Int,
    numberOfComments: Int,
    onMoreClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onMoreClicked() }
            .padding(start = (depth * 4).dp, top = 4.dp, bottom = 4.dp)
            .drawBehind {
                if (depth != 0) {
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = size.height),
                        color = when (depth % 4) {
                            0 -> Color.Magenta
                            1 -> Color.Blue
                            2 -> Color.Green
                            else -> Color.Red
                        },
                        strokeWidth = 3.0F,
                        alpha = 0.75F
                    )
                }
            }
    ) {
        Divider(modifier = Modifier.fillMaxWidth())
        Text(
            text = "more comments ($numberOfComments)",
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
        )
        Divider(modifier = Modifier.fillMaxWidth())
    }
}