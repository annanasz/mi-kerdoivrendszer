package com.bme.surveysystemsupportedbyai.ui.register.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.components.ProgressBar
import com.bme.surveysystemsupportedbyai.domain.model.Response
import com.bme.surveysystemsupportedbyai.domain.model.Response.Loading
import com.bme.surveysystemsupportedbyai.ui.register.RegisterViewModel

@Composable
fun SendEmailVerification(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    when(val sendEmailVerificationResponse = viewModel.sendEmailVerificationResponse) {
        is Loading -> ProgressBar()
        is Response.Success -> Unit
        is Response.Failure -> sendEmailVerificationResponse.apply {
            LaunchedEffect(e) {
                print(e)
            }
        }
    }
}