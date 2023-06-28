package com.example.lurkforreddit.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lurkforreddit.ui.screens.HomeScreen
import com.example.lurkforreddit.ui.screens.HomeViewModel
import com.example.lurkforreddit.ui.screens.CommentsScreen
import com.example.lurkforreddit.ui.screens.CommentsScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun LurkApp(
    homeViewModel: HomeViewModel,
    commentsScreenViewModel: CommentsScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable(route = "home") {
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

            commentsScreenViewModel.setSubreddit(subreddit)
            commentsScreenViewModel.setArticle(article)

            LaunchedEffect(Unit) {
                commentsScreenViewModel.loadPostComments()
            }
            CommentsScreen(
                uiState = commentsScreenViewModel.uiState.collectAsStateWithLifecycle(),
                subreddit = subreddit,
                article = article,
                onDetailsBackClicked = {
                    navController.popBackStack()
                    commentsScreenViewModel.clearNetworkRequest()
                },
                onSortChanged = { sort ->
                    coroutineScope.launch {
                        commentsScreenViewModel.setCommentSort(sort)
                    }
                }
            )
        }
    }
}