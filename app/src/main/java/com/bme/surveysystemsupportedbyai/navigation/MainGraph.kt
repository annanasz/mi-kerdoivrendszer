package com.bme.surveysystemsupportedbyai.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bme.surveysystemsupportedbyai.core.Constants
import com.bme.surveysystemsupportedbyai.ui.filledoutsurveys.FilledOutSurveyScreen
import com.bme.surveysystemsupportedbyai.ui.filloutwithspeech.FillOutWithSpeechScreen
import com.bme.surveysystemsupportedbyai.ui.inboxsurveys.InboxSurveyScreen
import com.bme.surveysystemsupportedbyai.ui.mysurveys.MySurveysSurveyScreen
import com.bme.surveysystemsupportedbyai.ui.mysurveys.SURVEY_ID
import com.bme.surveysystemsupportedbyai.ui.scansurveyscreen.ScanSurveyScreen
import com.bme.surveysystemsupportedbyai.ui.sentsurveydetails.SentSurveyDetailsScreen
import com.bme.surveysystemsupportedbyai.ui.sentsurveys.SentSurveysSurveyScreen
import com.bme.surveysystemsupportedbyai.ui.surveyDetails.SurveyDetailsScreen
import com.bme.surveysystemsupportedbyai.ui.surveyedit.SurveyEditScreen
import com.bme.surveysystemsupportedbyai.ui.surveyfillout.SurveyFillOutScreen


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
            MySurveysSurveyScreen(openDetailsScreen = { route -> navController.navigate(route) }, openScanSurveyScreen = {navController.navigate("${Screen.ScanSurveyScreen.route}?$SURVEY_ID=${""}")}, paddingValues)
        }
        composable(
            route = Screen.SentSurveysScreen.route
        ) {
            SentSurveysSurveyScreen(openDetailsScreen = { surveyId -> navController.navigate("${Screen.SentSurveyDetailsScreen.route}?$SURVEY_ID=${surveyId}") })
        }
        composable(
            route = Screen.FilledOutSurveysScreen.route
        ) {
            FilledOutSurveyScreen(openDetailsScreen = { route -> navController.navigate(route) }, paddingValues = paddingValues)
        }
        composable(
            route = Screen.InboxSurveysScreen.route
        ) {
            InboxSurveyScreen(openFillOutScreen = { route -> navController.navigate(route) })
        }
        composable("${Screen.SurveyEditScreen.route}$SURVEY_ID_ARG") {
            val previousDestination = navController.previousBackStackEntry?.destination?.route
            navController.previousBackStackEntry?.destination?.arguments?.get(
                SURVEY_ID)

            val navigateBack: () -> Unit = when (previousDestination) {
                Screen.MySurveysScreen.route -> {
                    { navController.popBackStack() }
                }
                "ScanSurvey?surveyId={surveyId}" ->{
                    {
                        navController.navigate("${Screen.ScanSurveyScreen.route}$SURVEY_ID_ARG")
                    }
                }
                else -> {

                    {navController.navigate(Screen.MySurveysScreen.route)}                }
            }
            val deleteSurvey: Boolean = when(previousDestination){
                "ScanSurvey?surveyId={surveyId}" -> {true}
                else -> {false}
            }

            SurveyEditScreen(navigateBack = navigateBack, navigateToMySurveys = {
                navController.navigate(
                    Screen.MySurveysScreen.route
                )
            }, openScanner={surveyId -> navController.navigate("${Screen.ScanSurveyScreen.route}?$SURVEY_ID=${surveyId}")}, deleteSurvey = deleteSurvey)
        }
        composable("${Screen.SurveyFillOutScreen.route}$SURVEY_ID_ARG$RECEIVED_ID_ARG") {
            SurveyFillOutScreen(navigateBack = {
                navController.popBackStack()
            })
        }
        composable("${Screen.FillOutSurveyWithSpeechScreen.route}$SURVEY_ID_ARG$RECEIVED_ID_ARG") {
            FillOutWithSpeechScreen(navigateBack = {
                navController.popBackStack()
            })
        }
        composable(
           "${Screen.SurveyDetailsScreen.route}$SURVEY_ID_ARG$RESPONSE_ID_ARG",
            arguments = listOf(
                navArgument("surveyId"){
                    defaultValue="surveyId"
                    type = NavType.StringType
                }, navArgument("responseNeeded"){
                    defaultValue=""
                    type= NavType.StringType
                }
            )
        ){
            SurveyDetailsScreen(navigateBack = {
                navController.popBackStack()
            })
        }
        composable("${Screen.ScanSurveyScreen.route}$SURVEY_ID_ARG",
            arguments = listOf(
                navArgument("surveyId"){
                    defaultValue=""
                    type= NavType.StringType
                }
            )){
            ScanSurveyScreen(navigateBack = {
                navController.navigate(Screen.MySurveysScreen.route)
            }, openDetailsScreen = { route -> navController.navigate(route) })
        }
        composable("${Screen.SentSurveyDetailsScreen.route}$SURVEY_ID_ARG",  arguments = listOf(
            navArgument("surveyId"){
                defaultValue="none"
                type= NavType.StringType
            }
        )){
            SentSurveyDetailsScreen(navigateBack = {
                navController.popBackStack()
            }, openDetailsScreen = {
                surveyId, responseId ->
                navController.navigate("${Screen.SurveyDetailsScreen.route}?$SURVEY_ID=${surveyId}&${RESPONSE_ID}=${responseId}")
            })
        }
    }
}

const val SURVEY_ID = "surveyId"
const val RESPONSE_ID = "responseId"
const val RECEIVED_ID = "receivedId"
const val RECEIVED_ID_ARG = "&$RECEIVED_ID={$RECEIVED_ID}"
const val SURVEY_ID_ARG = "?$SURVEY_ID={$SURVEY_ID}"
const val RESPONSE_ID_ARG = "&$RESPONSE_ID={$RESPONSE_ID}"
