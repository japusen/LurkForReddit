package com.example.lurkforreddit.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lurkforreddit.ui.components.DuplicatesSortMenu
import com.example.lurkforreddit.ui.components.ListingFeed
import com.example.lurkforreddit.util.DuplicatesSort

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuplicatesScreen(
    uiState: State<DuplicatesUiState>,
    onPostClicked: (String, String) -> Unit,
    onBackClicked: () -> Unit,
    onSortChanged: (DuplicatesSort) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = {
                    Text(
                        text = "Other Discussions",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                      DuplicatesSortMenu(
                          selectedSort = uiState.value.sort,
                          onSortChanged = onSortChanged
                      )
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        when (uiState.value.networkResponse) {
            is DuplicatesNetworkRequest.Loading -> LoadingScreen(modifier)
            is DuplicatesNetworkRequest.Success -> {
                ListingFeed(
                    submissions = (uiState.value.networkResponse as DuplicatesNetworkRequest.Success)
                        .listingContent
                        .collectAsLazyPagingItems(),
                    onPostClicked = onPostClicked,
                    modifier = Modifier.padding(paddingValues = it )
                )
            }
            is DuplicatesNetworkRequest.Error -> ErrorScreen(modifier)
        }
    }
}