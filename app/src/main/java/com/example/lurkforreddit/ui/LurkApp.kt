package com.example.lurkforreddit.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lurkforreddit.R
import com.example.lurkforreddit.ui.screens.HomeScreen
import com.example.lurkforreddit.ui.screens.HomeViewModel
import com.example.lurkforreddit.ui.screens.PostCommentsViewModel
import com.example.lurkforreddit.ui.screens.PostDetailsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LurkApp(
    homeViewModel: HomeViewModel,
    postCommentsViewModel: PostCommentsViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
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
                    postCommentsViewModel.changeSubreddit(subreddit)
                    postCommentsViewModel.changeArticle(article)
                    LaunchedEffect(Unit) {
                        postCommentsViewModel.loadPostComments()
                    }
                    PostDetailsScreen(commentState = postCommentsViewModel.commentState)
                }
            }
        }
    }
}