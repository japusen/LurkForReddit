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
import com.example.lurkforreddit.util.TopSortItems
import com.example.lurkforreddit.util.UserContentType
import com.example.lurkforreddit.util.UserContentTypeItems
import com.example.lurkforreddit.util.UserListingSort
import com.example.lurkforreddit.util.UserListingSortItems

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



    IconButton(
        onClick = { contentExpanded = true }
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

            for (item in UserContentTypeItems.values()) {
                DropdownMenuItem(
                    text = { Text(item.text) },
                    onClick = {
                        onContentTypeChanged(item.contentType)
                        contentExpanded = false
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(id = item.iconID),
                            contentDescription = null
                        )
                    },
                    colors =
                    if (contentType == item.contentType)
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

            for (item in UserListingSortItems.values().slice(0..1)) {
                DropdownMenuItem(
                    text = { Text(item.text) },
                    onClick = {
                        onSortChanged(item.sort, null)
                        sortExpanded = false
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(id = item.iconID),
                            contentDescription = null
                        )
                    },
                    colors =
                    if (selectedSort == item.sort)
                        MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.primary,
                            leadingIconColor = MaterialTheme.colorScheme.primary
                        )
                    else
                        MenuDefaults.itemColors()
                )
            }

            for (item in UserListingSortItems.values().slice(2..3)) {
                DropdownMenuItem(
                    text = { Text(item.text) },
                    onClick = {
                        submenuExpanded = true
                        sortExpanded = false
                        topOrControversial = item.sort
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(id = item.iconID),
                            contentDescription = null
                        )
                    },
                    colors =
                    if (selectedSort == item.sort)
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

            for (item in TopSortItems.values()) {
                DropdownMenuItem(
                    text = { Text(item.text) },
                    onClick = {
                        onSortChanged(topOrControversial, item.sort)
                        sortExpanded = false
                        submenuExpanded = false
                    }
                )
            }
        }
    }
}