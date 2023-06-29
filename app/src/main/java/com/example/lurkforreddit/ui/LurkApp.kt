package com.example.lurkforreddit.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lurkforreddit.ui.screens.CommentsScreen
import com.example.lurkforreddit.ui.screens.CommentsViewModel
import com.example.lurkforreddit.ui.screens.DuplicatesScreen
import com.example.lurkforreddit.ui.screens.DuplicatesViewModel
import com.example.lurkforreddit.ui.screens.HomeScreen
import com.example.lurkforreddit.ui.screens.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun LurkApp(
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable(route = "home") {
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
            HomeScreen(
                uiState = homeViewModel.uiState.collectAsStateWithLifecycle(),
                onListingSortChanged = { listing, top ->
                    coroutineScope.launch{
                        homeViewModel.setListingSort(listing, top)
                    }
                },
                onPostClicked = { subreddit, article ->
                    navController.navigate("details/$subreddit/$article")
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
            CommentsScreen(
                uiState = commentsViewModel.uiState.collectAsStateWithLifecycle(),
                subreddit = subreddit,
                onDetailsBackClicked = {
                    navController.popBackStack()
                    //commentsViewModel.clearNetworkRequest()
                },
                onSortChanged = { sort ->
                    coroutineScope.launch {
                        commentsViewModel.setCommentSort(sort)
                    }
                },
                onDuplicatesClicked = {
                    navController.navigate("duplicates/$subreddit/$article")
                }
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
            DuplicatesScreen(
                uiState = duplicatesViewModel.uiState.collectAsStateWithLifecycle(),
                onPostClicked = { sub, art ->
                    navController.navigate("details/$sub/$art")
                },
                onBackClicked = {
                    navController.popBackStack()
                },
                onSortChanged = { sort ->
                    coroutineScope.launch {
                        duplicatesViewModel.setSort(sort)
                    }
                }
            )
        }
    }
}