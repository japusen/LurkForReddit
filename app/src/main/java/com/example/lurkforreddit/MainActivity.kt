package com.example.lurkforreddit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.lurkforreddit.ui.LurkApp
import com.example.lurkforreddit.theme.LurkForRedditTheme


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