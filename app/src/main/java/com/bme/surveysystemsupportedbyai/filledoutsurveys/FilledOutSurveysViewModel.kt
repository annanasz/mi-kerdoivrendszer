package com.bme.surveysystemsupportedbyai.filledoutsurveys

import androidx.lifecycle.ViewModel
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FilledOutSurveysViewModel @Inject constructor(
    private val repo: AuthRepository,
    surveysRepository: SurveysRepository
): ViewModel() {

    val filledOutSurveys = surveysRepository.filledOutSurveys

    fun signOut() = repo.signOut()
    fun onItemClick( response:SurveyResponse,openScreen: (String) -> Unit){
        openScreen("${Screen.SurveyDetailsScreen.route}?$SURVEY_ID=${response.surveyId}&$RESPONSE_ID=${response.id}")
    }
}

const val SURVEY_ID = "surveyId"
const val RESPONSE_ID="responseId"