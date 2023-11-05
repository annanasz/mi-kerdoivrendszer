package com.bme.surveysystemsupportedbyai.surveyedit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.Question
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.SURVEY_ID
import com.bme.surveysystemsupportedbyai.surveyDetails.idFromParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val surveysRepository: SurveysRepository
) : ViewModel() {
    val survey = mutableStateOf(Survey())

    init {
        val surveyId =savedStateHandle.get<String>(SURVEY_ID) //"GKRvrEUl5u80XHJNMbqT"
        if (surveyId != null) {
            viewModelScope.launch {
                survey.value =
                    surveysRepository.getSurvey(surveyId.idFromParameter()) ?: Survey()
            }
        }
    }

    fun updateQuestionTitle(question: Question, newTitle: String) {
        val questionIndex = survey.value.questions.indexOf(question)
        if(questionIndex>=0 && questionIndex<survey.value.questions.size) {
            val updatedSurvey =
                survey.value.copy(questions = survey.value.questions.toMutableList().also { list ->
                    list[questionIndex] = list[questionIndex].copy(text = newTitle)
                })
            survey.value = updatedSurvey
        }
    }

    fun updateQuestionOption(question: Question, optionIndex: Int, newOption: String) {
        val questionIndex = survey.value.questions.indexOf(question)
        if(questionIndex>=0 && questionIndex<survey.value.questions.size) {
            val updatedSurvey =
                survey.value.copy(questions = survey.value.questions.toMutableList().also { list ->
                    list[questionIndex] = list[questionIndex].copy(
                        options = list[questionIndex].options.toMutableList().also { options ->
                            options[optionIndex] = newOption
                        })
                })
            survey.value = updatedSurvey
        }
    }
    fun deleteQuestion(question:Question){
        val deleteSurvey = survey.value.copy(questions = survey.value.questions.toMutableList().also { list ->
            list.remove(question)
        })
        survey.value = deleteSurvey
    }

    fun addNewOption(question:Question){
        survey.value = survey.value.copy(questions = survey.value.questions.mapIndexed { index, q ->
            if (q == question) {
                q.copy(options = q.options.toMutableList().apply { add("") })
            } else {
                q
            }
        })
    }

    fun removeOption(question:Question, option:String){
        survey.value = survey.value.copy(questions = survey.value.questions.mapIndexed { index, q ->
            if (q == question) {
                q.copy(options = q.options.toMutableList().apply { remove(option) })
            } else {
                q
            }
        })
    }

    fun addQuestion(questionType: String){
        var list = listOf("")
        if(questionType == "short_answer")
            list = emptyList()
        val newQuestion = Question(
            type = questionType,
            options = list,
            isRequired = true,
            text = ""
        )

        val updatedSurvey = survey.value.copy(
            questions = survey.value.questions.toMutableList().also { list ->
                list.add(newQuestion)
            }
        )

        survey.value = updatedSurvey
    }
    fun updateQuestionIsRequired(question:Question, isChecked:Boolean){
        val questionIndex = survey.value.questions.indexOf(question)
        if(questionIndex>=0 && questionIndex<survey.value.questions.size) {
        if (questionIndex >= 0 && questionIndex < survey.value.questions.size) {
            val updatedSurvey = survey.value.copy(questions = survey.value.questions.toMutableList().also { list ->
                list[questionIndex] = list[questionIndex].copy(isRequired = isChecked)
            })
            survey.value = updatedSurvey
        }}
    }

    fun onDoneClick(popUpScreen: () -> Unit) {
        viewModelScope.launch {
            val editedSurvey = survey.value
            if (editedSurvey.id.isBlank()) {
                surveysRepository.saveSurvey(editedSurvey)
            } else {
                surveysRepository.updateSurvey(editedSurvey)
            }
            popUpScreen()
        }
    }

    fun onBackClick(navigateBack: ()->Unit, deleteSurvey: Boolean){
        if(deleteSurvey && survey.value.id.isNotEmpty())
            viewModelScope.launch {
                surveysRepository.deleteSurvey(survey.value.id)
            }
        navigateBack()
    }

    fun scanQuestion(openScanner: (String)->Unit){
        openScanner(survey.value.id)
    }
}