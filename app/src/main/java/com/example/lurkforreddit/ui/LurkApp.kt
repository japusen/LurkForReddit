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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.lurkforreddit.R
import com.example.lurkforreddit.ui.screens.HomeScreen
import com.example.lurkforreddit.ui.screens.LurkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LurkApp(
    lurkViewModel: LurkViewModel,
    modifier: Modifier = Modifier
) {
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
            HomeScreen(
                lurkUiState = lurkViewModel.lurkUiState
            )
        }
    }
}