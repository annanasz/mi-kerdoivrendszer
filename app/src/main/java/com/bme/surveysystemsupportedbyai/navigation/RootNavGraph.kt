package com.bme.surveysystemsupportedbyai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bme.surveysystemsupportedbyai.home.HomeScreen

@Composable
fun RootNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = startDestination
    ) {
        authGraph(navController = navController)
        composable(route = Graph.MAIN) {
            HomeScreen()
        }
    }
}

