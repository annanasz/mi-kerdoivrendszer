package com.bme.surveysystemsupportedbyai.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bme.surveysystemsupportedbyai.ui.home.components.BottomNavigation
import com.bme.surveysystemsupportedbyai.navigation.MainGraph
import com.bme.surveysystemsupportedbyai.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val shouldShowBottomNavigation = navBackStackEntry?.destination?.route?.contains(Screen.SurveyDetailsScreen.route)==false &&
            navBackStackEntry?.destination?.route?.contains(Screen.SurveyEditScreen.route)==false &&
            navBackStackEntry?.destination?.route?.contains(Screen.SurveyFillOutScreen.route)==false &&
            navBackStackEntry?.destination?.route?.contains(Screen.ScanSurveyScreen.route) == false &&
            navBackStackEntry?.destination?.route?.contains(Screen.FillOutSurveyWithSpeechScreen.route) == false

    if(shouldShowBottomNavigation){
        Scaffold(
            bottomBar = {BottomNavigation(navController = navController, viewModel.selectedItemIndex.intValue
            ) { destination -> viewModel.navigate(destination) }
            }
        ){
                paddingValues ->
            MainGraph(navController = navController, paddingValues = paddingValues)
        }
    }
    else{
        Scaffold {
                paddingValues ->
            MainGraph(navController = navController, paddingValues = paddingValues)
        }
    }

}