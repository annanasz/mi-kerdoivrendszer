package com.bme.surveysystemsupportedbyai.ui.sign_in.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.domain.model.Response
import com.bme.surveysystemsupportedbyai.components.ProgressBar
import com.bme.surveysystemsupportedbyai.ui.sign_in.SignInViewModel
import com.google.firebase.auth.FirebaseAuthException

@Composable
fun SignIn(
    viewModel: SignInViewModel = hiltViewModel(),
    showErrorMessage: (errorMessage: String?) -> Unit
) {
    when(val signInResponse = viewModel.signInResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> Unit
        is Response.Failure -> signInResponse.apply {
            LaunchedEffect(e) {
                when(e){
                    is FirebaseAuthException -> {
                        when (e.errorCode){
                            "ERROR_INVALID_CREDENTIAL" -> showErrorMessage("Invalid credentials! Wrong email or password" )
                            "ERROR_INVALID_EMAIL" -> showErrorMessage("The email address is badly formatted!")
                            "ERROR_WRONG_PASSWORD" -> showErrorMessage("Invalid credentials! Wrong email or password" )
                            "ERROR_MISSING_EMAIL" -> showErrorMessage("Missing email!" )
                        }
                    }
                    else -> showErrorMessage(e.message)
                }
            }
        }
    }
}