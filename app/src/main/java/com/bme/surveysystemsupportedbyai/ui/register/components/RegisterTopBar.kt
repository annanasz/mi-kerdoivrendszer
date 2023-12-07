package com.bme.surveysystemsupportedbyai.ui.register.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.bme.surveysystemsupportedbyai.components.BackIcon
import com.bme.surveysystemsupportedbyai.core.Constants.REGISTER_SCREEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar (
        title = {
            Text(
                text = REGISTER_SCREEN
            )
        },
        navigationIcon = {
            BackIcon(
                navigateBack = navigateBack
            )
        }
    )
}