package com.bme.surveysystemsupportedbyai.ui.sentsurveydetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.Answer
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.SURVEY_ID
import com.bme.surveysystemsupportedbyai.ui.surveyDetails.DetailsScreenUiState
import com.bme.surveysystemsupportedbyai.ui.surveyDetails.DetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentSurveyDetailsViewModel@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val surveysRepository: SurveysRepository
) : ViewModel(), DetailsViewModel {

    private val _uiState = MutableStateFlow(SentSurveyDetailsScreenUiState())
    override val uiState: StateFlow<DetailsScreenUiState> = _uiState.asStateFlow()

    private var surveyId:String?
    private var loaded = false

    var selectedTab by mutableIntStateOf(0)
    override fun onOptionSelected(option: String) {
    }

    init {
        surveyId =savedStateHandle.get<String>(SURVEY_ID)
    }

    fun fetchData() {
        val localSurveyId = surveyId
        if (!localSurveyId.isNullOrEmpty() && !loaded) {
            viewModelScope.launch {
                val survey = surveysRepository.getSurvey(localSurveyId) ?: Survey()
                val responsesWithAnswer =
                    surveysRepository.getResponsesWithAnswers(localSurveyId) ?: emptyList()
                _uiState.value =
                    _uiState.value.copy(survey = survey, responses = responsesWithAnswer)
                loaded = true
            }
        }
    }
    private fun createAnswerMap(answers: List<Answer?>): Map<String, Answer> {
        val answerMap = mutableStateMapOf<String, Answer>()

        for (answer in answers) {
            if(answer!=null)
                answerMap[answer.questionId] = answer
        }

        return answerMap
    }
}

data class SentSurveyDetailsScreenUiState(
    override val survey: Survey = Survey(),
    override val response: SurveyResponse = SurveyResponse(),
    override val answers: Map<String, Answer> = emptyMap(),
    val responses: List<SurveyResponse?> = emptyList()
): DetailsScreenUiState