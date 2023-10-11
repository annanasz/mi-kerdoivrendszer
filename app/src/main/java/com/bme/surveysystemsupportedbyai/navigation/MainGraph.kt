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
import com.bme.surveysystemsupportedbyai.surveyfillout.SurveyFillOutScreen

@OptIn(ExperimentalComposeApi::class)
@Composable
fun MainGraph(
    navController: NavHostController, paddingValues: PaddingValues
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
            InboxSurveyScreen(openFillOutScreen = { route -> navController.navigate(route) })
        }
        composable("${Screen.SurveyDetailsScreen.route}$SURVEY_ID_ARG") {
            SurveyDetailsScreen(navigateBack = {
                navController.popBackStack()
            })
        }
        composable("${Screen.SurveyEditScreen.route}$SURVEY_ID_ARG") {
            SurveyEditScreen(navigateBack = {
                navController.popBackStack()
            })
        }
        composable("${Screen.SurveyFillOutScreen.route}$SURVEY_ID_ARG") {
            SurveyFillOutScreen(navigateBack = {
                navController.popBackStack()
            })
        }
    }
}

const val SURVEY_ID = "surveyId"
const val SURVEY_ID_ARG = "?$SURVEY_ID={$SURVEY_ID}"
