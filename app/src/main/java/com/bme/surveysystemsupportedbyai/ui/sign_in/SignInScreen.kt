package com.bme.surveysystemsupportedbyai.ui.sign_in

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.core.Utils.Companion.showMessage
import com.bme.surveysystemsupportedbyai.ui.sign_in.components.SignIn
import com.bme.surveysystemsupportedbyai.ui.sign_in.components.SignInContent
import com.bme.surveysystemsupportedbyai.ui.sign_in.components.SignInTopBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
@ExperimentalComposeApi
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigateToRegisterScreen:()->Unit
){
    val context = LocalContext.current
    Scaffold(
        topBar = {
            SignInTopBar()
        },
        content = { padding ->
            SignInContent(
                padding = padding,
                signIn = { email, password ->
                    viewModel.signInWithEmailAndPassword(email, password)
                },
                navigateToRegisterScreen = navigateToRegisterScreen
            )
        }
    )

    SignIn(
        showErrorMessage = { errorMessage ->
            showMessage(context, errorMessage)
        }
    )
}