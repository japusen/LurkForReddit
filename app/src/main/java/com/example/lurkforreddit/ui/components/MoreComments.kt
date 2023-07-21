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
import com.example.lurkforreddit.model.More

@Composable
fun MoreComments(
    padding: Int,
    color: Int,
    more: More,
) {
    Column(
        modifier = Modifier
            .clickable { /* TODO load more comments */ }
            .padding(start = padding.dp)
            .drawBehind {
                if (color != 0) {
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = size.height),
                        color = when (color % 4) {
                            0 -> Color.Magenta
                            1 -> Color.Blue
                            2 -> Color.Green
                            else -> Color.Red
                        },
                        strokeWidth = 2F,
                        alpha = 0.5F
                    )
                }
            }
    ) {
        Divider(modifier = Modifier.fillMaxWidth())
        Text(
            text = "more comments (${more.count})",
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
        )
    }
}