package com.bme.surveysystemsupportedbyai.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.components.TopBar
import com.bme.surveysystemsupportedbyai.core.Constants.HOME_SCREEN
import com.bme.surveysystemsupportedbyai.home.components.HomeContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            TopBar(
                title = HOME_SCREEN,
                signOut = {
                    viewModel.signOut()
                }
            )
        },
        content = { padding ->
            HomeContent(
                padding = padding
            )
        }
    )

}