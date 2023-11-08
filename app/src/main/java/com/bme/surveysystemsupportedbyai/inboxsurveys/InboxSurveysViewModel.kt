package com.bme.surveysystemsupportedbyai.inboxsurveys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.ReceivedSurvey
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.mysurveys.SURVEY_ID
import com.bme.surveysystemsupportedbyai.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboxSurveysViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val surveysRepository: SurveysRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(InboxSurveysUiState())
    val uiState: StateFlow<InboxSurveysUiState> = _uiState.asStateFlow()

    fun fetchData() {
        viewModelScope.launch {
            try {
                surveysRepository.receivedSurveys.collect { surveys ->
                    _uiState.value = _uiState.value.copy(receivedSurveys = surveys)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun onFillOutClick(receivedSurvey: ReceivedSurvey, openFillOutScreen: (String) -> Unit){
        openFillOutScreen("${Screen.SurveyFillOutScreen.route}?$SURVEY_ID={${receivedSurvey.surveyId}}")
    }
    fun onFillOutWithSpeechClick(receivedSurvey: ReceivedSurvey, openFillOutScreen: (String) -> Unit){
        openFillOutScreen("${Screen.FillOutSurveyWithSpeechScreen.route}?$SURVEY_ID={${receivedSurvey.surveyId}}")
    }
    fun signOut() = repo.signOut()

}
data class InboxSurveysUiState(
    val receivedSurveys: List<ReceivedSurvey> = emptyList(),
    val errorMessage: String? = null
)

const val SURVEY_ID = "surveyId"