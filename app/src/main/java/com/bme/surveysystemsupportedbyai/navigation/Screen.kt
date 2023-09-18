package com.bme.surveysystemsupportedbyai.navigation

import com.bme.surveysystemsupportedbyai.core.Constants.HOME_SCREEN
import com.bme.surveysystemsupportedbyai.core.Constants.REGISTER_SCREEN
import com.bme.surveysystemsupportedbyai.core.Constants.SIGN_IN_SCREEN

sealed class Screen(val route: String) {
    object SignInScreen:Screen(SIGN_IN_SCREEN)
    object RegisterScreen: Screen(REGISTER_SCREEN)
    object HomeScreen: Screen(HOME_SCREEN)
}