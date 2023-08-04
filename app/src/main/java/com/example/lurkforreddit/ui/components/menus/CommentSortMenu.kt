package com.example.lurkforreddit.ui.components.menus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lurkforreddit.R
import com.example.lurkforreddit.util.CommentSort


@Composable
fun CommentSortMenu(
    selectedSort: CommentSort,
    onSortChanged: (CommentSort) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded = !expanded }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_sort),
            contentDescription = "Sort Comments"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(8.dp)
        ) {
            Text(
                text = "Comment Sort",
                textAlign = TextAlign.Center
            )

            DropdownMenuItem(
                text = { Text("Top") },
                onClick = {
                    onSortChanged(CommentSort.TOP)
                    expanded = !expanded
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_rising),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == CommentSort.TOP)
                    MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                else
                    MenuDefaults.itemColors()
            )
            DropdownMenuItem(
                text = { Text("Best") },
                onClick = {
                    onSortChanged(CommentSort.BEST)
                    expanded = !expanded
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_star),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == CommentSort.BEST)
                    MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                else
                    MenuDefaults.itemColors()
            )
            DropdownMenuItem(
                text = { Text("New") },
                onClick = {
                    onSortChanged(CommentSort.NEW)
                    expanded = !expanded
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_time),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == CommentSort.NEW)
                    MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                else
                    MenuDefaults.itemColors()
            )
            DropdownMenuItem(
                text = { Text("Controversial") },
                onClick = {
                    onSortChanged(CommentSort.CONTROVERSIAL)
                    expanded = !expanded
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_controversial),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == CommentSort.CONTROVERSIAL)
                    MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                else
                    MenuDefaults.itemColors()
            )
            DropdownMenuItem(
                text = { Text("Q&A") },
                onClick = {
                    onSortChanged(CommentSort.QA)
                    expanded = !expanded
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_question_answer),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == CommentSort.QA)
                    MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                else
                    MenuDefaults.itemColors()
            )
        }
    }
}
