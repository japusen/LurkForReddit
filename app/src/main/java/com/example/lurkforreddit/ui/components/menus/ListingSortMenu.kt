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
import com.example.lurkforreddit.util.ListingSort
import com.example.lurkforreddit.util.TopSort

@Composable
fun ListingSortMenu(
    selectedSort: ListingSort,
    onListingSortChanged: (ListingSort, TopSort?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var submenuExpanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_sort),
            contentDescription = "Sort Posts"
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
                text = "Post Sort",
                textAlign = TextAlign.Center
            )

            DropdownMenuItem(
                text = { Text("Hot") },
                onClick = {
                    onListingSortChanged(ListingSort.HOT, null)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_hot),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == ListingSort.HOT)
                    MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                else
                    MenuDefaults.itemColors()

            )
            DropdownMenuItem(
                text = { Text("Rising") },
                onClick = {
                    onListingSortChanged(ListingSort.RISING, null)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_rising),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == ListingSort.RISING)
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
                    onListingSortChanged(ListingSort.NEW, null)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_time),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == ListingSort.NEW)
                    MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                else
                    MenuDefaults.itemColors()
            )
            DropdownMenuItem(
                text = { Text("Top") },
                onClick = {
                    submenuExpanded = true
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_top),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == ListingSort.TOP)
                    MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                else
                    MenuDefaults.itemColors()
            )
        }
    }

    DropdownMenu(
        expanded = submenuExpanded,
        onDismissRequest = { submenuExpanded = false },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(8.dp)
        ) {
            Text(
                text = "Top",
                textAlign = TextAlign.Center
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = "...",
                    )
                },
                onClick = {
                    expanded = true
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("All") },
                onClick = {
                    onListingSortChanged(ListingSort.TOP, TopSort.ALL)
                    expanded = false
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Year") },
                onClick = {
                    onListingSortChanged(ListingSort.TOP, TopSort.YEAR)
                    expanded = false
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Month") },
                onClick = {
                    onListingSortChanged(ListingSort.TOP, TopSort.MONTH)
                    expanded = false
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Week") },
                onClick = {
                    onListingSortChanged(ListingSort.TOP, TopSort.WEEK)
                    expanded = false
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Day") },
                onClick = {
                    onListingSortChanged(ListingSort.TOP, TopSort.DAY)
                    expanded = false
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Hour") },
                onClick = {
                    onListingSortChanged(ListingSort.TOP, TopSort.HOUR)
                    expanded = false
                    submenuExpanded = false
                }
            )
        }
    }
}