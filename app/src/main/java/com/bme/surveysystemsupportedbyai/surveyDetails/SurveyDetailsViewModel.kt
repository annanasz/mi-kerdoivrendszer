package com.bme.surveysystemsupportedbyai.surveyDetails

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.Answer
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.RESPONSE_ID
import com.bme.surveysystemsupportedbyai.navigation.SURVEY_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val surveysRepository: SurveysRepository
) : ViewModel(), DetailsScreenViewModel {
    override val survey = mutableStateOf(Survey())
    override val response = mutableStateOf(SurveyResponse())
    override val answers = mutableStateOf(emptyMap<String,Answer>())

    init {
        val responseId = savedStateHandle.get<String>(RESPONSE_ID)
        val surveyId =savedStateHandle.get<String>(SURVEY_ID)
        if (!surveyId.isNullOrEmpty()) {
            viewModelScope.launch {
                if(!responseId.isNullOrEmpty()) {
                    response.value = surveysRepository.getResponse(responseId) ?: SurveyResponse()
                    answers.value=createAnswerMap(response.value.answers)
                }
                survey.value =
                    surveysRepository.getSurvey(surveyId) ?: Survey()

            }
        }
    }

    override fun onOptionSelected(option:String){

    }
}
fun String.idFromParameter(): String {
    return this.substring(1, this.length - 1)
}
fun createAnswerMap(answers: List<Answer?>): Map<String, Answer> {
    val answerMap = mutableStateMapOf<String, Answer>()

    for (answer in answers) {
        if(answer!=null)
            answerMap[answer.questionId] = answer
    }

    return answerMap
}