package com.bme.surveysystemsupportedbyai.navigation

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.ui.register.RegisterScreen
import com.bme.surveysystemsupportedbyai.ui.sign_in.SignInScreen
import com.bme.surveysystemsupportedbyai.ui.theme.AppTheme

@OptIn(ExperimentalComposeApi::class)
fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.AUTH,
        startDestination = Constants.SIGN_IN_SCREEN
    ) {
        composable(
            route = Screen.SignInScreen.route
        ) {
            AppTheme {
                SignInScreen(
                    navigateToRegisterScreen = {
                        navController.navigate(Screen.RegisterScreen.route)
                    }
                )
            }
        }
        composable(
            route = Screen.RegisterScreen.route
        ) {
            AppTheme {
                RegisterScreen(
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}