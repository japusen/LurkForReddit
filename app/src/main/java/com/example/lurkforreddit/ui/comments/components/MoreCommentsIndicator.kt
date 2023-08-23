package com.example.lurkforreddit.ui.comments.components

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
            .padding(start = (depth * 8).dp, top = 4.dp, bottom = 4.dp)
            .drawBehind {
                if (depth != 0) {
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = size.height),
                        color = when (depth % 7) {
                            0 -> Color.Magenta
                            1 -> Color(75, 0, 130)
                            2 -> Color.Blue
                            3 -> Color.Green
                            4 -> Color.Yellow
                            5 -> Color(255, 127, 0)
                            6 -> Color.Red
                            else -> Color(148, 0, 211)
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