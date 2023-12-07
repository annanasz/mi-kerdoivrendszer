package com.bme.surveysystemsupportedbyai.ui.filledoutsurveys

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FilledOutSurveysViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val surveysRepository: SurveysRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(FilledOutSurveysUiState())
    val uiState: StateFlow<FilledOutSurveysUiState> = _uiState.asStateFlow()
    val loading = mutableStateOf(true)

    fun fetchData() {
        viewModelScope.launch {
            try {
                surveysRepository.filledOutSurveys.collect { surveys ->
                    _uiState.value = _uiState.value.copy(filledOutSurveys = surveys)
                    loading.value = false
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
                loading.value = false
            }
        }
    }

    fun signOut() = repo.signOut()
    fun onItemClick( response:SurveyResponse,openScreen: (String) -> Unit){
        openScreen("${Screen.SurveyDetailsScreen.route}?$SURVEY_ID=${response.surveyId}&$RESPONSE_ID=${response.id}")
    }
}

data class FilledOutSurveysUiState(
    val filledOutSurveys: List<SurveyResponse> = emptyList(),
    val errorMessage: String? = null
)

const val SURVEY_ID = "surveyId"
const val RESPONSE_ID="responseId"