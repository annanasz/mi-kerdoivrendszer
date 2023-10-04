package com.bme.surveysystemsupportedbyai.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.filledoutsurveys.FilledOutSurveyScreen
import com.bme.surveysystemsupportedbyai.inboxsurveys.InboxSurveyScreen
import com.bme.surveysystemsupportedbyai.mysurveys.MySurveysSurveyScreen
import com.bme.surveysystemsupportedbyai.sentsurveys.SentSurveysSurveyScreen
import com.bme.surveysystemsupportedbyai.surveyDetails.SurveyDetailsScreen
import com.bme.surveysystemsupportedbyai.surveyedit.SurveyEditScreen

@OptIn(ExperimentalComposeApi::class)
@Composable
fun MainGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = Constants.MY_SURVEYS_SCREEN
    ) {
        composable(
            route = Screen.MySurveysScreen.route
        ) {
            MySurveysSurveyScreen(openDetailsScreen = { route -> navController.navigate(route) })
        }
        composable(
            route = Screen.SentSurveysScreen.route
        ) {
            SentSurveysSurveyScreen()
        }
        composable(
            route = Screen.FilledOutSurveysScreen.route
        ) {
            FilledOutSurveyScreen()
        }
        composable(
            route = Screen.InboxSurveysScreen.route
        ) {
            InboxSurveyScreen()
        }
        composable(Screen.SurveyDetailsScreen.route) {
            SurveyDetailsScreen(navigateBack = {
                navController.popBackStack()
            })
        }
        composable(Screen.SurveyEditScreen.route) {
            SurveyEditScreen()
        }
    }
}
