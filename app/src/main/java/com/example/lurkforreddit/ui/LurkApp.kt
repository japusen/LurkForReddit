package com.example.lurkforreddit.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lurkforreddit.R
import com.example.lurkforreddit.ui.components.ImageLink
import com.example.lurkforreddit.ui.components.VideoPlayer
import com.example.lurkforreddit.ui.components.menus.DuplicatesSortMenu
import com.example.lurkforreddit.ui.components.menus.ListingSortMenu
import com.example.lurkforreddit.ui.components.menus.ProfileSortMenu
import com.example.lurkforreddit.ui.screens.CommentsScreen
import com.example.lurkforreddit.ui.screens.CommentsViewModel
import com.example.lurkforreddit.ui.screens.DuplicatesViewModel
import com.example.lurkforreddit.ui.screens.HomeScreen
import com.example.lurkforreddit.ui.screens.HomeViewModel
import com.example.lurkforreddit.ui.screens.ListingScreen
import com.example.lurkforreddit.ui.screens.ListingViewModel
import com.example.lurkforreddit.ui.screens.ProfileViewModel
import com.example.lurkforreddit.util.openLinkInBrowser
import com.example.lurkforreddit.util.openPostLink
import kotlinx.coroutines.launch

@Composable
fun LurkApp() {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable(route = "home") {
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
            val homeUiState = homeViewModel.uiState.collectAsStateWithLifecycle()

            HomeScreen(
                query = homeUiState.value.query,
                subreddit = homeUiState.value.subreddit,
                selectedSort = homeUiState.value.listingSort,
                networkResponse = homeUiState.value.networkResponse,
                searchResults = homeUiState.value.searchResults,
                setQuery = { query ->
                    homeViewModel.setQuery(query)
                },
                clearQuery = { homeViewModel.clearQuery() },
                updateSearchResults = {
                    coroutineScope.launch {
                        homeViewModel.updateSearchResults()
                    }
                },
                onListingSortChanged = { listing, top ->
                    coroutineScope.launch{
                        homeViewModel.setListingSort(listing, top)
                    }
                },
                onPostClicked = { subreddit, article ->
                    navController.navigate("details/$subreddit/$article")
                },
                onProfileClicked = { username ->
                    navController.navigate("user/$username")
                },
                onSubredditClicked = { subreddit ->
                    navController.navigate("subreddit/$subreddit")
                },
                onBrowserClicked = { url ->
                    openLinkInBrowser(context, url)
                },
                onLinkClicked = { url ->
                    openPostLink(context, navController, url)
                }
            )
        }
        composable(
            route = "subreddit/{subreddit}",
            arguments = listOf(
                navArgument("subreddit") { type = NavType.StringType },
            )
        ) {
            val listingViewModel: ListingViewModel = viewModel(factory = ListingViewModel.Factory)
            val subredditUiState = listingViewModel.uiState.collectAsStateWithLifecycle()

            ListingScreen(
                title = listingViewModel.subreddit,
                onBackClicked = { navController.popBackStack() },
                networkResponse = subredditUiState.value.networkResponse,
                onPostClicked = { subreddit, article ->
                    navController.navigate("details/$subreddit/$article")
                },
                onProfileClicked = { username ->
                    navController.navigate("user/$username")
                },
                onSubredditClicked = {}, /* Already on the sub, no need to open again*/
                onBrowserClicked = { url ->
                    openLinkInBrowser(context, url)
                },
                onLinkClicked = { url ->
                    openPostLink(context, navController, url)
                }
            ) {
                ListingSortMenu(
                    selectedSort = subredditUiState.value.listingSort,
                    onListingSortChanged = { listing, top ->
                        coroutineScope.launch{
                            listingViewModel.setListingSort(listing, top)
                        }
                    }
                )
            }
        }
        composable(
            route = "user/{username}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
            )
        ) {
            val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
            val profileUiState = profileViewModel.uiState.collectAsStateWithLifecycle()

            ListingScreen(
                title = profileViewModel.username,
                networkResponse = profileUiState.value.networkResponse,
                onBackClicked = { navController.popBackStack() },
                onPostClicked = { subreddit, article ->
                    navController.navigate("details/$subreddit/$article")
                },
                onProfileClicked = {}, /* Already on userpage, no need to open again */
                onSubredditClicked = { subreddit ->
                    navController.navigate("subreddit/$subreddit")
                },
                onBrowserClicked = { url ->
                    openLinkInBrowser(context, url)
                },
                onLinkClicked = { url ->
                    openPostLink(context, navController, url)
                }
            ) {
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
            }
        }
        composable(
            route = "duplicates/{subreddit}/{article}",
            arguments = listOf(
                navArgument("subreddit") { type = NavType.StringType },
                navArgument("article") { type = NavType.StringType }
            )
        ) {

            val duplicatesViewModel: DuplicatesViewModel =
                viewModel(factory = DuplicatesViewModel.Factory)
            val duplicatesUiState = duplicatesViewModel.uiState.collectAsStateWithLifecycle()

            ListingScreen(
                title = stringResource(R.string.other_discussions),
                networkResponse = duplicatesUiState.value.networkResponse,
                onBackClicked = { navController.popBackStack() },
                onPostClicked = { subreddit, article ->
                    navController.navigate("details/$subreddit/$article")
                },
                onProfileClicked = { username ->
                    navController.navigate("user/$username")
                },
                onSubredditClicked = { subreddit ->
                    navController.navigate("subreddit/$subreddit")
                },
                onBrowserClicked = { url ->
                    openLinkInBrowser(context, url)
                },
                onLinkClicked = { url ->
                    openPostLink(context, navController, url)
                }
            ) {
                DuplicatesSortMenu(
                    selectedSort = duplicatesUiState.value.sort,
                    onSortChanged = { sort ->
                        coroutineScope.launch {
                            duplicatesViewModel.setSort(sort)
                        }
                    }
                )
            }
        }
        composable(
            route = "details/{subreddit}/{article}",
            arguments = listOf(
                navArgument("subreddit") { type = NavType.StringType },
                navArgument("article") { type = NavType.StringType }
            )
        ) {

            val commentsViewModel: CommentsViewModel =
                viewModel(factory = CommentsViewModel.Factory)
            val commentsUiState = commentsViewModel.uiState.collectAsStateWithLifecycle()

            CommentsScreen(
                subreddit = commentsViewModel.subreddit,
                selectedSort = commentsUiState.value.commentSort,
                networkResponse = commentsUiState.value.networkResponse,
                onBackClicked = { navController.popBackStack() },
                onDuplicatesClicked = {
                    navController.navigate("duplicates" +
                        "/${commentsViewModel.subreddit}" +
                        "/${commentsViewModel.article}")
                },
                onSortChanged = { sort ->
                    coroutineScope.launch {
                        commentsViewModel.setCommentSort(sort)
                    }
                },
                onLinkClicked = { url ->
                    openPostLink(context, navController, url)
                },
                onProfileClicked = { username ->
                    navController.navigate("user/$username")
                },
                onMoreClicked = {
                    coroutineScope.launch {
                        commentsViewModel.getMoreComments()
                    }
                },
                onNestedMoreClicked = { id ->
                    /*TODO
                    coroutineScope.launch {
                        commentsViewModel.getMoreComments(id)
                    }*/
                }
            )
        }
        composable(
            route = "image/{url}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            ImageLink(url)
        }
        composable(
            route = "video/{url}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            VideoPlayer(url.toUri())
        }
    }
}