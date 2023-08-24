package com.example.lurkforreddit.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
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
import com.example.lurkforreddit.ui.comments.CommentsScreen
import com.example.lurkforreddit.ui.comments.CommentsViewModel
import com.example.lurkforreddit.ui.common.ImageLink
import com.example.lurkforreddit.ui.common.ListingSortMenu
import com.example.lurkforreddit.ui.common.VideoPlayer
import com.example.lurkforreddit.ui.common.screens.ListingScreen
import com.example.lurkforreddit.ui.duplicateposts.DuplicatePostsViewModel
import com.example.lurkforreddit.ui.duplicateposts.DuplicatesSortMenu
import com.example.lurkforreddit.ui.home.HomeScreen
import com.example.lurkforreddit.ui.home.HomeViewModel
import com.example.lurkforreddit.ui.profile.ProfileSortMenu
import com.example.lurkforreddit.ui.profile.ProfileViewModel
import com.example.lurkforreddit.ui.subreddit.SubredditViewModel
import com.example.lurkforreddit.util.openLinkInBrowser
import com.example.lurkforreddit.util.openPostLink

@Composable
fun LurkApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "home",
    ) {

        composable(
            route = "home",
        ) {
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
            val homeUiState = homeViewModel.uiState.collectAsStateWithLifecycle()

            HomeScreen(
                query = homeUiState.value.query,
                subreddit = homeUiState.value.subreddit,
                selectedSort = homeUiState.value.listingSort,
                networkResponse = homeUiState.value.networkResponse,
                searchResult = homeUiState.value.searchResult,
                setQuery = { query ->
                    homeViewModel.setQuery(query)
                },
                clearQuery = { homeViewModel.clearQuery() },
                updateSearchResults = {
                    homeViewModel.updateSearchResults()
                },
                onListingSortChanged = { listing, top ->
                    homeViewModel.setListingSort(listing, top)
                },
                onPostClicked = { subreddit, article ->
                    navController.navigate("comments/$subreddit/$article")
                },
                onProfileClicked = { username ->
                    navController.navigate("user/$username")
                },
                onSubredditClicked = { subreddit ->
                    navController.navigate("subreddit/$subreddit")
                },
                onBrowserClicked = { url, domain ->
                    openLinkInBrowser(context, url, domain)
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
            ),
        ) {
            val subredditViewModel: SubredditViewModel = viewModel(factory = SubredditViewModel.Factory)
            val subredditUiState = subredditViewModel.uiState.collectAsStateWithLifecycle()

            val toast = Toast.makeText(context, "Already viewing ${subredditViewModel.subreddit}", Toast.LENGTH_SHORT)

            ListingScreen(
                title = subredditViewModel.subreddit,
                onBackClicked = { navController.popBackStack() },
                networkResponse = subredditUiState.value.networkResponse,
                onPostClicked = { subreddit, article ->
                    navController.navigate("comments/$subreddit/$article")
                },
                onProfileClicked = { username ->
                    navController.navigate("user/$username")
                },
                onSubredditClicked = { toast.show() }, /* Already on the sub, no need to open again*/
                onBrowserClicked = { url, domain ->
                    openLinkInBrowser(context, url, domain)
                },
                onLinkClicked = { url ->
                    openPostLink(context, navController, url)
                }
            ) {
                ListingSortMenu(
                    selectedSort = subredditUiState.value.listingSort,
                    onListingSortChanged = { listing, top ->
                        subredditViewModel.setListingSort(listing, top)
                    }
                )
            }
        }

        composable(
            route = "user/{username}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
            ),
        ) {
            val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
            val profileUiState = profileViewModel.uiState.collectAsStateWithLifecycle()

            val toast = Toast.makeText(context, "Already viewing ${profileViewModel.username}'s profile", Toast.LENGTH_SHORT)

            ListingScreen(
                title = profileViewModel.username,
                networkResponse = profileUiState.value.networkResponse,
                onBackClicked = { navController.popBackStack() },
                onPostClicked = { subreddit, article ->
                    navController.navigate("comments/$subreddit/$article")
                },
                onProfileClicked = { toast.show()}, /* Already on userpage, no need to open again */
                onSubredditClicked = { subreddit ->
                    navController.navigate("subreddit/$subreddit")
                },
                onBrowserClicked = { url, domain ->
                    openLinkInBrowser(context, url, domain)
                },
                onLinkClicked = { url ->
                    openPostLink(context, navController, url)
                }
            ) {
                ProfileSortMenu(
                    selectedSort = profileUiState.value.userListingSort,
                    contentType = profileUiState.value.contentType,
                    onContentTypeChanged = { contentType ->
                        profileViewModel.setContentType(contentType)
                    },
                    onSortChanged = { sort, top ->
                        profileViewModel.setListingSort(sort, top)
                    }
                )
            }
        }

        composable(
            route = "duplicates/{subreddit}/{article}",
            arguments = listOf(
                navArgument("subreddit") { type = NavType.StringType },
                navArgument("article") { type = NavType.StringType }
            ),
        ) {

            val duplicatePostsViewModel: DuplicatePostsViewModel =
                viewModel(factory = DuplicatePostsViewModel.Factory)
            val duplicatesUiState = duplicatePostsViewModel.uiState.collectAsStateWithLifecycle()

            ListingScreen(
                title = stringResource(R.string.other_discussions),
                networkResponse = duplicatesUiState.value.networkResponse,
                onBackClicked = { navController.popBackStack() },
                onPostClicked = { subreddit, article ->
                    navController.navigate("comments/$subreddit/$article")
                },
                onProfileClicked = { username ->
                    navController.navigate("user/$username")
                },
                onSubredditClicked = { subreddit ->
                    navController.navigate("subreddit/$subreddit")
                },
                onBrowserClicked = { url, domain ->
                    openLinkInBrowser(context, url, domain)
                },
                onLinkClicked = { url ->
                    openPostLink(context, navController, url)
                }
            ) {
                DuplicatesSortMenu(
                    selectedSort = duplicatesUiState.value.sort,
                    onSortChanged = { sort ->
                        duplicatePostsViewModel.setSort(sort)
                    }
                )
            }
        }

        composable(
            route = "comments/{subreddit}/{article}",
            arguments = listOf(
                navArgument("subreddit") { type = NavType.StringType },
                navArgument("article") { type = NavType.StringType }
            ),
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
                    commentsViewModel.setCommentSort(sort)
                },
                onLinkClicked = { url ->
                    openPostLink(context, navController, url)
                },
                onProfileClicked = { username ->
                    navController.navigate("user/$username")
                },
                onBrowserClicked = { url, domain ->
                    openLinkInBrowser(context, url, domain)
                },
                onChangeVisibility = { show, start, depth ->
                    commentsViewModel.changeCommentVisibility(show, start, depth)
                },
                onMoreClicked = { index ->
                    commentsViewModel.getMoreComments(index)
                }
            )
        }

        composable(
            route = "image/{url}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            ImageLink(
                url = url,
                onBackClicked = { navController.popBackStack() },
            )
        }

        composable(
            route = "video/{url}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            VideoPlayer(
                uri = url.toUri(),
                onBackClicked = { navController.popBackStack() },
            )
        }
    }
}