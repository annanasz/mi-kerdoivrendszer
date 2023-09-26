package com.bme.surveysystemsupportedbyai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bme.surveysystemsupportedbyai.core.Constants.AUTH_NESTED_ROUTE
import com.bme.surveysystemsupportedbyai.core.Constants.HOME_NESTED_ROUTE
import com.bme.surveysystemsupportedbyai.navigation.NavGraph
import com.bme.surveysystemsupportedbyai.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            NavGraph(
                navController=navController
            )
            AuthState()
        }
    }

    @Composable
    private fun AuthState() {
        val isUserSignedOut = viewModel.getAuthState().collectAsState().value
        if (isUserSignedOut) {
            NavigateToSignInScreen()
        } else {
            /*if (viewModel.isEmailVerified) {
                //NavigateToProfileScreen()
            } else {
                //NavigateToVerifyEmailScreen()
            }*/
            NavigateToHomeScreen()
        }
    }
    @Composable
    private fun NavigateToSignInScreen() = navController.navigate(AUTH_NESTED_ROUTE) {
        popUpTo(HOME_NESTED_ROUTE) {
            inclusive = true
        }
    }

    @Composable
    private fun NavigateToHomeScreen() = navController.navigate(HOME_NESTED_ROUTE) {
        popUpTo(AUTH_NESTED_ROUTE) {
            inclusive = true
        }
    }

}

