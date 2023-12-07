package com.bme.surveysystemsupportedbyai.core

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.bme.surveysystemsupportedbyai.R

object Constants {
    val titleFontFamily = FontFamily(
        Font(R.font.changaone_regular)
    )

    const val SIGN_IN_SCREEN = "Sign in"
    const val REGISTER_SCREEN = "Register"
    const val MY_SURVEYS_SCREEN = "MY SURVEYS"
    const val SENT_SURVEYS_SCREEN = "SENT SURVEYS"
    const val FILLED_OUT_SURVEYS_SCREEN = "FILLED OUT SURVEYS"
    const val INBOX_SURVEYS_SCREEN = "INBOX"
    const val SURVEY_DETAILS_SCREEN = "SurveyDetails"
    const val SURVEY_EDIT_SCREEN = "SurveyEdit"
    const val SURVEY_FILL_OUT_SCREEN = "SurveyFillOut"
    const val SCAN_SURVEY_SCREEN = "ScanSurvey"
    const val SENT_SURVEY_DETAILS_SCREEN = "SentSurveyDetailsScreen"
    const val FILL_OUT_SURVEY_WITH_SPEECH = "FillOutWithSpeech"
    //Menu Items
    const val SIGN_OUT_ITEM = "Sign out"


    const val EMAIL_LABEL = "Email"
    const val PASSWORD_LABEL = "Password"

    //Useful
    const val EMPTY_STRING = ""

    const val NO_ACCOUNT = "No account? Sign up."
    const val ALREADY_USER = "Already a user? Sign in."

    //Buttons
    const val SIGN_IN_BUTTON = "Sign in"
    const val REGISTER_BUTTON = "Register"

    const val VERIFY_EMAIL_MESSAGE = "We've sent you an email with a link to verify the email."


    const val SCAN_MESSAGE="Scan your survey"

    const val TEXT_RECOGNITION_ERROR_MESSAGE = "An error occurred, text recognition model is not initialized yet!"
    const val IMAGE_PROC_ERROR_MESSAGE = "An error occurred during processing"

}