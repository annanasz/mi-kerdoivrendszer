package com.bme.surveysystemsupportedbyai.ui.surveyfillout

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.Answer
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.RECEIVED_ID
import com.bme.surveysystemsupportedbyai.navigation.SURVEY_ID
import com.bme.surveysystemsupportedbyai.ui.surveyDetails.idFromParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyFillOutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val surveysRepository: SurveysRepository
): ViewModel(){
    val survey = mutableStateOf(Survey())
    val done = mutableStateOf(false)
    private val selectedOptions = mutableStateOf<Map<String,List<String>>>(emptyMap())
    private var receivedId = ""

    init {
        val surveyId =savedStateHandle.get<String>(SURVEY_ID)
        receivedId = savedStateHandle.get<String>(RECEIVED_ID)?.idFromParameter().toString()
        if (surveyId != null) {
            viewModelScope.launch {
                survey.value =
                    surveysRepository.getSurvey(surveyId.idFromParameter()) ?: Survey()
            }
        }
    }
    fun updateSelectedOption(questionId: String, selectedOption: String) {
        val updatedSelectedOptions = selectedOptions.value.toMutableMap()

        val question = survey.value.questions.find { it.id == questionId }
        if (question != null && question.type == "checkbox") {
            val selectedOptionsForQuestion = updatedSelectedOptions[questionId]?.toMutableSet() ?: mutableSetOf()
            selectedOptionsForQuestion.add(selectedOption)
            updatedSelectedOptions[questionId] = selectedOptionsForQuestion.toList()
        } else {
            updatedSelectedOptions[questionId] = listOf(selectedOption)
        }
        selectedOptions.value = updatedSelectedOptions
    }
    fun removeSelectedOption(questionId: String, selectedOptionToRemove: String) {
        val updatedSelectedOptions = selectedOptions.value.toMutableMap()
        if (updatedSelectedOptions.containsKey(questionId)) {
            val selectedOptionsForQuestion = updatedSelectedOptions[questionId]?.toMutableSet() ?: mutableSetOf()
            selectedOptionsForQuestion.remove(selectedOptionToRemove)
            if(selectedOptionsForQuestion.isEmpty())
                updatedSelectedOptions.remove(questionId)
            else
                updatedSelectedOptions[questionId] = selectedOptionsForQuestion.toList()

            selectedOptions.value = updatedSelectedOptions
        }
    }

    fun allRequiredQuestionsAnswered():Boolean{
        return survey.value.questions.all { question ->
            !question.isRequired || selectedOptions.value.containsKey(question.id)
        }
    }

    fun sendFilledOutSurvey(){
        val surveyResponse = SurveyResponse(
            surveyId = survey.value.id,
            surveyTitle = survey.value.title,
            answers = selectedOptions.value.map { (questionId, selectedOption) ->
                Answer(
                    questionId = questionId,
                    type = "multiple_choice",
                    response = selectedOption
                )
            }
        )
        viewModelScope.launch {
            surveysRepository.fillOutSurvey(surveyResponse, receivedId)
            if(receivedId.isNotEmpty())
                surveysRepository.updateReceivedSurvey(receivedId)
            done.value = true
        }
    }
}