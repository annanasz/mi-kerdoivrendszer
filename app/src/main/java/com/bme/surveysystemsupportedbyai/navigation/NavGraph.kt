package com.bme.surveysystemsupportedbyai.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        startDestination = Screen.SignInScreen.route
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
        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen()
        }
    }
}