package com.bme.surveysystemsupportedbyai.navigation

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.register.RegisterScreen
import com.bme.surveysystemsupportedbyai.sign_in.SignInScreen

@OptIn(ExperimentalComposeApi::class)
fun NavGraphBuilder.AuthGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.AUTH,
        startDestination = Constants.SIGN_IN_SCREEN
    ) {
        composable(
            route = Screen.SignInScreen.route
        ) {
            SignInScreen(
                navigateToRegisterScreen = {
                    navController.navigate(Screen.RegisterScreen.route)
                }
            )
        }
        composable(
            route = Screen.RegisterScreen.route
        ) {
            RegisterScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}