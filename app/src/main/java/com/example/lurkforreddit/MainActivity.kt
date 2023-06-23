package com.example.lurkforreddit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lurkforreddit.ui.LurkApp
import com.example.lurkforreddit.ui.screens.HomeViewModel
import com.example.lurkforreddit.ui.screens.PostDetailsViewModel
import com.example.lurkforreddit.ui.theme.LurkForRedditTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LurkForRedditTheme {
                LurkApp()
            }
        }
    }

}