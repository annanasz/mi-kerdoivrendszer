package com.bme.surveysystemsupportedbyai.inboxsurveys

import androidx.lifecycle.ViewModel
import com.bme.surveysystemsupportedbyai.domain.model.ReceivedSurvey
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.mysurveys.SURVEY_ID
import com.bme.surveysystemsupportedbyai.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InboxSurveysViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val surveysRepository: SurveysRepository
): ViewModel() {

    val receivedSurveys = surveysRepository.receivedSurveys
    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false
    fun onFillOutClick(receivedSurvey: ReceivedSurvey, openFillOutScreen: (String) -> Unit){
        openFillOutScreen("${Screen.SurveyFillOutScreen.route}?$SURVEY_ID={${receivedSurvey.surveyId}}")
    }
    fun signOut() = repo.signOut()

}
const val SURVEY_ID = "surveyId"