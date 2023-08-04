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
import com.example.lurkforreddit.util.TopSort
import com.example.lurkforreddit.util.UserContentType
import com.example.lurkforreddit.util.UserListingSort

@Composable
fun ProfileSortMenu(
    selectedSort: UserListingSort,
    contentType: UserContentType,
    onContentTypeChanged: (UserContentType) -> Unit,
    onSortChanged: (UserListingSort, TopSort?) -> Unit,
    modifier: Modifier = Modifier
) {
    var contentExpanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }
    var submenuExpanded by remember { mutableStateOf(false) }
    var topOrControversial by remember { mutableStateOf(UserListingSort.TOP) }



    IconButton(onClick = { contentExpanded = true }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_content_type),
            contentDescription = "Content Type"
        )
    }

    IconButton(
        onClick = { sortExpanded = true }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_sort),
            contentDescription = "Sort content"
        )
    }

    DropdownMenu(
        expanded = contentExpanded,
        onDismissRequest = { contentExpanded = false },
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(8.dp)
        ) {
            Text(
                text = "Profile",
                textAlign = TextAlign.Center
            )

            DropdownMenuItem(
                text = { Text("Submissions") },
                onClick = {
                    onContentTypeChanged(UserContentType.SUBMITTED)
                    contentExpanded = false
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_submissions),
                        contentDescription = null
                    )
                },
                colors =
                if (contentType == UserContentType.SUBMITTED)
                    MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                else
                    MenuDefaults.itemColors()
            )
            DropdownMenuItem(
                text = { Text("Comments") },
                onClick = {
                    onContentTypeChanged(UserContentType.COMMENTS)
                    contentExpanded = false
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_comment),
                        contentDescription = null
                    )
                },
                colors =
                if (contentType == UserContentType.COMMENTS)
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
        expanded = sortExpanded,
        onDismissRequest = { sortExpanded = false },
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(8.dp)
        ) {
            Text(
                text = if (contentType == UserContentType.SUBMITTED) "Sort Posts"
                else "Sort Comments",
                textAlign = TextAlign.Center
            )

            DropdownMenuItem(
                text = { Text("Hot") },
                onClick = {
                    onSortChanged(UserListingSort.HOT, null)
                    sortExpanded = false
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_hot),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == UserListingSort.HOT)
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
                    onSortChanged(UserListingSort.NEW, null)
                    sortExpanded = false
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_time),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == UserListingSort.NEW)
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
                    submenuExpanded = true
                    sortExpanded = false
                    topOrControversial = UserListingSort.CONTROVERSIAL
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_controversial),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == UserListingSort.CONTROVERSIAL)
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
                    sortExpanded = false
                    topOrControversial = UserListingSort.TOP
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_top),
                        contentDescription = null
                    )
                },
                colors =
                if (selectedSort == UserListingSort.TOP)
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
                    sortExpanded = true
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("All") },
                onClick = {
                    onSortChanged(topOrControversial, TopSort.ALL)
                    sortExpanded = false
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Year") },
                onClick = {
                    onSortChanged(topOrControversial, TopSort.YEAR)
                    sortExpanded = false
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Month") },
                onClick = {
                    onSortChanged(topOrControversial, TopSort.MONTH)
                    sortExpanded = false
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Week") },
                onClick = {
                    onSortChanged(topOrControversial, TopSort.WEEK)
                    sortExpanded = false
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Day") },
                onClick = {
                    onSortChanged(topOrControversial, TopSort.DAY)
                    sortExpanded = false
                    submenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Hour") },
                onClick = {
                    onSortChanged(topOrControversial, TopSort.HOUR)
                    sortExpanded = false
                    submenuExpanded = false
                }
            )
        }
    }
}