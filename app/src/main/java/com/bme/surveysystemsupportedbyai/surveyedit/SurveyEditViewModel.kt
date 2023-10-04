package com.bme.surveysystemsupportedbyai.surveyedit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.bme.surveysystemsupportedbyai.domain.model.Question
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.model.User
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SurveyEditViewModel @Inject constructor(
    private val surveysRepository: SurveysRepository
) : ViewModel() {
    val survey = mutableStateOf(Survey())

    init {// majd ha megy a firestore, itt le kell keni az adott id-val rendelkezo taskot, most csak dummy adatok
        var survey_new = Survey(
            creator = User(email = "valami", surveys = listOf()), questions = listOf(
                Question(
                    type = "multiple_choice",
                    options = listOf("elso", "masodik", "harmadik", "negyedik"),
                    isRequired = true,
                    text = "ez egy proba multiple_choice"
                ),
                Question(
                    type = "multiple_choice",
                    options = listOf("elso", "masodik", "harmadik", "negyedik"),
                    isRequired = true,
                    text = "ez a masodik proba multiple_choice"
                ),
                Question(
                    type = "checkbox",
                    options = listOf("elso", "masodik", "harmadik", "negyedik"),
                    isRequired = true,
                    text = "ez egy proba checkbox"
                ),
                Question(
                    type = "checkbox",
                    options = listOf("elso", "masodik", "harmadik", "negyedik"),
                    isRequired = true,
                    text = "ez egy masik proba checkbox"
                ),
                Question(
                    type = "short_answer",
                    isRequired = true,
                    options = listOf(),
                    text = "ez egy rovid valasz"
                )
            ), responses = listOf(), timestamp = "2022.10.02", title = "Teszt3"
        )
        survey.value = survey_new
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

}