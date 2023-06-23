package com.example.lurkforreddit.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lurkforreddit.ui.screens.HomeScreen
import com.example.lurkforreddit.ui.screens.HomeViewModel
import com.example.lurkforreddit.ui.screens.PostDetailsScreen
import com.example.lurkforreddit.ui.screens.PostDetailsViewModel
import kotlinx.coroutines.launch

@Composable
fun LurkApp(
    homeViewModel: HomeViewModel,
    postDetailsViewModel: PostDetailsViewModel,
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
                listingState = homeViewModel.listingState,
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
            LaunchedEffect(Unit) {
                postDetailsViewModel.loadPostComments(subreddit, article)
            }
            PostDetailsScreen(
                detailsState = postDetailsViewModel.detailsState,
                subreddit = subreddit,
                article = article,
                onDetailsBackClicked = {
                    postDetailsViewModel.clearData()
                    navController.popBackStack()
                },
                onSortChanged = { sort ->
                    postDetailsViewModel.clearData()
                    coroutineScope.launch {
                        postDetailsViewModel.changeCommentSort(subreddit, article, sort)
                    }
                }
            )
        }
    }
}