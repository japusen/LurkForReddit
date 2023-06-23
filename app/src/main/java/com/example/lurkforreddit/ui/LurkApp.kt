package com.example.lurkforreddit.ui

import androidx.compose.runtime.Composable
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

@Composable
fun LurkApp(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    postDetailsViewModel: PostDetailsViewModel = viewModel(factory = PostDetailsViewModel.Factory),
    ) {
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
            PostDetailsScreen(
                postDetailsViewModel = postDetailsViewModel,
                subreddit = subreddit,
                article = article,
                onDetailsBackClicked = { navController.popBackStack() }
            )
        }
    }
}