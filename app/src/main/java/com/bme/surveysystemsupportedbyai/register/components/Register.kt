package com.bme.surveysystemsupportedbyai.register.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.components.ProgressBar
import com.bme.surveysystemsupportedbyai.domain.model.Response
import com.bme.surveysystemsupportedbyai.register.RegisterViewModel

@Composable
fun Register(
    viewModel: RegisterViewModel = hiltViewModel(),
    sendEmailVerification: () -> Unit,
    showVerifyEmailMessage: () -> Unit
) {
    when(val registerResponse = viewModel.registerResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success<*> -> {
            val isUserRegistered = registerResponse.data as Boolean
            LaunchedEffect(isUserRegistered) {
                if (isUserRegistered) {
                    sendEmailVerification()
                    showVerifyEmailMessage()
                }
            }
        }
        is Response.Failure -> registerResponse.apply {
            LaunchedEffect(e) {
                print(e)
            }
        }
    }
}