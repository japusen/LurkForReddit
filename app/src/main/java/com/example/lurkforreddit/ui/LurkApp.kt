package com.example.lurkforreddit.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lurkforreddit.R
import com.example.lurkforreddit.ui.components.menus.CommentSortMenu
import com.example.lurkforreddit.ui.components.menus.DuplicatesSortMenu
import com.example.lurkforreddit.ui.components.menus.ListingSortMenu
import com.example.lurkforreddit.ui.components.menus.ProfileSortMenu
import com.example.lurkforreddit.ui.screens.CommentsScreen
import com.example.lurkforreddit.ui.screens.CommentsViewModel
import com.example.lurkforreddit.ui.screens.DuplicatesViewModel
import com.example.lurkforreddit.ui.screens.ListingScreen
import com.example.lurkforreddit.ui.screens.ListingViewModel
import com.example.lurkforreddit.ui.screens.ProfileViewModel
import com.example.lurkforreddit.util.openLinkInBrowser
import kotlinx.coroutines.launch

@Composable
fun LurkApp(
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable(route = "home") {
            val listingViewModel: ListingViewModel = viewModel(factory = ListingViewModel.Factory)
            val homeUiState = listingViewModel.uiState.collectAsStateWithLifecycle()
            ListingScreen(
                title = {
                    Text(
                        text = homeUiState.value.subreddit,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navIcon = {},
                actions = {
                    ListingSortMenu(
                        selectedSort = homeUiState.value.listingSort,
                        onListingSortChanged = { listing, top ->
                            coroutineScope.launch{
                                listingViewModel.setListingSort(listing, top)
                            }
                        }
                    )
                },
                networkResponse = homeUiState.value.networkResponse,
                onPostClicked = { subreddit, article ->
                    navController.navigate("details/$subreddit/$article")
                },
                onProfileClicked = { username ->
                    navController.navigate("user/$username")
                },
                onSubredditClicked = { sub ->
                    navController.navigate("subreddit/$sub")
                },
                onBrowserClicked = { url ->
                    openLinkInBrowser(context, url)
                },
            )
        }
        composable(
            route = "subreddit/{subreddit}",
            arguments = listOf(
                navArgument("subreddit") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val subreddit = backStackEntry.arguments?.getString("subreddit") ?: ""

            val listingViewModel: ListingViewModel = viewModel(factory = ListingViewModel.Factory)
            LaunchedEffect(Unit) {
                listingViewModel.setSubreddit(subreddit)
            }

            val subredditUiState = listingViewModel.uiState.collectAsStateWithLifecycle()
            ListingScreen(
                title = {
                    Text(
                        text = subredditUiState.value.subreddit,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    ListingSortMenu(
                        selectedSort = subredditUiState.value.listingSort,
                        onListingSortChanged = { listing, top ->
                            coroutineScope.launch{
                                listingViewModel.setListingSort(listing, top)
                            }
                        }
                    )
                },
                networkResponse = subredditUiState.value.networkResponse,
                onPostClicked = { sub, article ->
                    navController.navigate("details/$sub/$article")
                },
                onProfileClicked = { username ->
                    navController.navigate("user/$username")
                },
                onSubredditClicked = {}, /* Already on the sub, no need to reload*/
                onBrowserClicked = { url ->
                    openLinkInBrowser(context, url)
                },
            )
        }
        composable(
            route = "user/{username}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""

            val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
            LaunchedEffect(Unit) {
                profileViewModel.setUser(username)
            }

            val profileUiState = profileViewModel.uiState.collectAsStateWithLifecycle()
            ListingScreen(
                title = {
                    Text(
                        text = profileUiState.value.user,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                      ProfileSortMenu(
                          selectedSort = profileUiState.value.userListingSort,
                          contentType = profileUiState.value.contentType,
                          onContentTypeChanged = { contentType ->
                              coroutineScope.launch {
                                  profileViewModel.setContentType(contentType)
                              }
                          },
                          onSortChanged = { sort, top ->
                              coroutineScope.launch{
                                  profileViewModel.setListingSort(sort, top)
                              }
                          }
                      )
                },
                networkResponse = profileUiState.value.networkResponse,
                onPostClicked = { sub, article ->
                    navController.navigate("details/$sub/$article")
                },
                onProfileClicked = {}, /* Already on userpage, no need to reload */
                onSubredditClicked = { sub ->
                    navController.navigate("subreddit/$sub")
                },
                onBrowserClicked = { url ->
                    openLinkInBrowser(context, url)
                },
            )
        }
        composable(
            route = "duplicates/{subreddit}/{article}",
            arguments = listOf(
                navArgument("subreddit") { type = NavType.StringType },
                navArgument("article") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val subreddit = backStackEntry.arguments?.getString("subreddit") ?: ""
            val article = backStackEntry.arguments?.getString("article") ?: ""

            val duplicatesViewModel: DuplicatesViewModel =
                viewModel(factory = DuplicatesViewModel.Factory)
            duplicatesViewModel.setSubreddit(subreddit)
            duplicatesViewModel.setArticle(article)


            LaunchedEffect(Unit) {
                duplicatesViewModel.loadDuplicates()
            }

            val duplicatesUiState = duplicatesViewModel.uiState.collectAsStateWithLifecycle()
            ListingScreen(
                title = {
                    Text(
                        text = "Other Discussions",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    DuplicatesSortMenu(
                        selectedSort = duplicatesUiState.value.sort,
                        onSortChanged = { sort ->
                            coroutineScope.launch {
                                duplicatesViewModel.setSort(sort)
                            }
                        }
                    )
                },
                networkResponse = duplicatesUiState.value.networkResponse,
                onPostClicked = { sub, art ->
                    navController.navigate("details/$sub/$art")
                },
                onProfileClicked = { username ->
                    navController.navigate("user/$username")
                },
                onSubredditClicked = { sub ->
                    navController.navigate("subreddit/$sub")
                },
                onBrowserClicked = { url ->
                    openLinkInBrowser(context, url)
                }
            )
        }
        composable(
            route = "details/{subreddit}/{article}",
            arguments = listOf(
                navArgument("subreddit") { type = NavType.StringType },
                navArgument("article") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val subreddit = backStackEntry.arguments?.getString("subreddit") ?: ""
            val article = backStackEntry.arguments?.getString("article") ?: ""

            val commentsViewModel: CommentsViewModel =
                viewModel(factory = CommentsViewModel.Factory)
            commentsViewModel.setSubreddit(subreddit)
            commentsViewModel.setArticle(article)

            LaunchedEffect(Unit) {
                commentsViewModel.loadPostComments()
            }

            val commentsUiState = commentsViewModel.uiState.collectAsStateWithLifecycle()
            CommentsScreen(
                title = {
                    Text(
                        text = subreddit,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    CommentSortMenu(
                        selectedSort = commentsUiState.value.commentSort,
                        onSortChanged = { sort ->
                            coroutineScope.launch {
                                commentsViewModel.setCommentSort(sort)
                            }
                        }
                    )
                    IconButton(
                        onClick = { navController.navigate("duplicates/$subreddit/$article") }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_other_discussions),
                            contentDescription = "Other Discussions"
                        )
                    }
                },
                networkResponse = commentsUiState.value.networkResponse
            )
        }
    }
}