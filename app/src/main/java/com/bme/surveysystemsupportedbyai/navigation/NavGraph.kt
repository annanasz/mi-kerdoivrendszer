package com.bme.surveysystemsupportedbyai.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.bme.surveysystemsupportedbyai.core.Constants.AUTH_NESTED_ROUTE
import com.bme.surveysystemsupportedbyai.core.Constants.HOME_NESTED_ROUTE
import com.bme.surveysystemsupportedbyai.home.HomeScreen
import com.bme.surveysystemsupportedbyai.register.RegisterScreen
import com.bme.surveysystemsupportedbyai.sign_in.SignInScreen

@OptIn(ExperimentalComposeApi::class)
@Composable
fun NavGraph(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = AUTH_NESTED_ROUTE
    ){
        navigation(
            startDestination = Screen.SignInScreen.route,
            route = AUTH_NESTED_ROUTE
        ){
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
        navigation(
            startDestination = Screen.HomeScreen.route,
            route= HOME_NESTED_ROUTE
        ){
            composable(
                route = Screen.HomeScreen.route
            ) {
                HomeScreen()
            }
        }
    }
}