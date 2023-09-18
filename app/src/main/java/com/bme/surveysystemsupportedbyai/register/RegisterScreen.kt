package com.bme.surveysystemsupportedbyai.register

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.bme.surveysystemsupportedbyai.core.Constants.VERIFY_EMAIL_MESSAGE
import com.bme.surveysystemsupportedbyai.core.Utils.Companion.showMessage
import com.bme.surveysystemsupportedbyai.register.components.Register
import com.bme.surveysystemsupportedbyai.register.components.RegisterContent
import com.bme.surveysystemsupportedbyai.register.components.RegisterTopBar
import com.bme.surveysystemsupportedbyai.register.components.SendEmailVerification

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            RegisterTopBar(
                navigateBack = navigateBack
            )
        },
        content = { padding ->
            RegisterContent(
                padding = padding,
                register = { email, password, passwordAgain ->
                    viewModel.registerWithEmailAndPassword(email, password, passwordAgain)
                },
                navigateBack = navigateBack
            )
        }
    )

    Register(
        sendEmailVerification = {
            viewModel.sendEmailVerification()
        },
        showVerifyEmailMessage = {
            showMessage(context, VERIFY_EMAIL_MESSAGE)
        }
    )

    SendEmailVerification()
}