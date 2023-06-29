package com.example.lurkforreddit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lurkforreddit.ui.LurkApp
import com.example.lurkforreddit.ui.screens.CommentsViewModel
import com.example.lurkforreddit.ui.screens.DuplicatesViewModel
import com.example.lurkforreddit.ui.screens.HomeViewModel
import com.example.lurkforreddit.ui.theme.LurkForRedditTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LurkForRedditTheme {
                val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
                val commentsViewModel: CommentsViewModel =
                    viewModel(factory = CommentsViewModel.Factory)
                val duplicatesViewModel: DuplicatesViewModel =
                    viewModel(factory = DuplicatesViewModel.Factory)
                LurkApp(
                    homeViewModel = homeViewModel,
                    commentsViewModel = commentsViewModel,
                    duplicatesViewModel = duplicatesViewModel
                )
            }
        }
    }
}