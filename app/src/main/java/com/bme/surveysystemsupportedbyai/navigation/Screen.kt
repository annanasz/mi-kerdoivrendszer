package com.bme.surveysystemsupportedbyai.navigation

import com.bme.surveysystemsupportedbyai.core.Constants.FILLED_OUT_SURVEYS_SCREEN
import com.bme.surveysystemsupportedbyai.core.Constants.HOME_SCREEN
import com.bme.surveysystemsupportedbyai.core.Constants.INBOX_SURVEYS_SCREEN
import com.bme.surveysystemsupportedbyai.core.Constants.MY_SURVEYS_SCREEN
import com.bme.surveysystemsupportedbyai.core.Constants.REGISTER_SCREEN
import com.bme.surveysystemsupportedbyai.core.Constants.SENT_SURVEYS_SCREEN
import com.bme.surveysystemsupportedbyai.core.Constants.SIGN_IN_SCREEN
import com.bme.surveysystemsupportedbyai.core.Constants.SURVEY_DETAILS_SCREEN
import com.bme.surveysystemsupportedbyai.core.Constants.SURVEY_EDIT_SCREEN
import com.bme.surveysystemsupportedbyai.core.Constants.SURVEY_FILL_OUT_SCREEN

sealed class Screen(val route: String) {
    object SignInScreen:Screen(SIGN_IN_SCREEN)
    object RegisterScreen: Screen(REGISTER_SCREEN)
    object HomeScreen: Screen(HOME_SCREEN)
    object MySurveysScreen: Screen(MY_SURVEYS_SCREEN)
    object SentSurveysScreen: Screen(SENT_SURVEYS_SCREEN)
    object FilledOutSurveysScreen: Screen(FILLED_OUT_SURVEYS_SCREEN)
    object InboxSurveysScreen: Screen(INBOX_SURVEYS_SCREEN)
    object SurveyDetailsScreen: Screen(SURVEY_DETAILS_SCREEN)
    object SurveyEditScreen: Screen(SURVEY_EDIT_SCREEN)
    object SurveyFillOutScreen: Screen(SURVEY_FILL_OUT_SCREEN)
}