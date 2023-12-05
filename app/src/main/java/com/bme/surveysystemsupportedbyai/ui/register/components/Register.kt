package com.bme.surveysystemsupportedbyai.ui.register.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.components.ProgressBar
import com.bme.surveysystemsupportedbyai.domain.model.Response
import com.bme.surveysystemsupportedbyai.ui.register.RegisterViewModel
import com.google.firebase.auth.FirebaseAuthException

@Composable
fun Register(
    viewModel: RegisterViewModel = hiltViewModel(),
    sendEmailVerification: () -> Unit,
    showVerifyEmailMessage: () -> Unit,
    showErrorMessage: (String?) -> Unit
) {
    when (val registerResponse = viewModel.registerResponse) {
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
                when (e) {
                    is FirebaseAuthException -> {
                        when (e.errorCode) {
                            "ERROR_CREDENTIAL_ALREADY_IN_USE" -> showErrorMessage("The email address is already registered!")
                            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> showErrorMessage("The email address is already registered!")
                            "ERROR_WEAK_PASSWORD" -> showErrorMessage("The password has to be at least 6 characters long!")
                            "ERROR_MISSING_EMAIL" -> showErrorMessage("Missing email!")
                        }
                    }

                    else -> showErrorMessage(e.message)
                }
            }
        }
    }
}