package com.bme.surveysystemsupportedbyai.scansurveyscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.domain.model.Question
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.mysurveys.SURVEY_ID
import com.bme.surveysystemsupportedbyai.navigation.Screen
import com.google.mlkit.vision.text.Text
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ScanSurveyViewModel @Inject constructor(private val surveysRepository: SurveysRepository) :
    ViewModel() {
    var savedSurveyId:String? = ""
    fun processTextResult(result: Text,callback: (String?) -> Unit){
        var title = ""
        val questions = mutableListOf<Question>()
        var actualQuestion: Question? = null
        for (block in result.textBlocks) {
            for (line in block.lines) {
                val lineText = line.text
                if (title.equals("")) title = lineText
                else {
                    if (lineText[0] != 'o' && lineText[0] != 'O' && lineText[0] != 'X' && lineText[0] != 'x') {
                        if (actualQuestion != null) {
                            questions.add(actualQuestion)
                            if (actualQuestion.type.equals("")) actualQuestion.type =
                                "short_answer"
                        }
                        actualQuestion = Question()
                        val lineTextCut = extractRestOfStringAndCheckAsterisk(lineText)
                        actualQuestion.isRequired = lineTextCut.first
                        actualQuestion.text = lineTextCut.second
                    } else if (lineText[0] == 'o' || lineText[0] == 'O') {
                        actualQuestion?.type = "multiple_choice"
                        val lineTextCut = removeLeadingOAndSpace(lineText)
                        val list: MutableList<String>? = actualQuestion?.options?.toMutableList()
                        list?.add(lineTextCut)
                        if (list != null) {
                            actualQuestion?.options = list
                        }
                    } else if (lineText[0] != 'X' || lineText[0] != 'x') {
                        actualQuestion?.type = "checkbox"
                        val lineTextCut = removeLeadingXAndSpace(lineText)
                        val list: MutableList<String>? = actualQuestion?.options?.toMutableList()
                        list?.add(lineTextCut)
                        if (list != null) {
                            actualQuestion?.options = list
                        }
                    }
                }
            }
        }
        if (actualQuestion != null) questions.add(actualQuestion)

        val survey = Survey(
            title = title,
            questions = questions,
        )
        viewModelScope.launch {
             savedSurveyId = surveysRepository.saveSurvey(survey)
            callback(savedSurveyId)
        }
    }
    fun openEditScreen(surveyId: String, openScreen: (String) -> Unit) {
        openScreen("${Screen.SurveyEditScreen.route}?$SURVEY_ID={${surveyId}}")
    }

    fun removeLeadingOAndSpace(input: String): String {
        return input.replace(regex = "^[oO]\\s*".toRegex(), replacement = "")
    }

    fun removeLeadingXAndSpace(input: String): String {
        return input.replace(regex = "^[xX]\\s*".toRegex(), replacement = "")
    }

    fun extractRestOfStringAndCheckAsterisk(input: String): Pair<Boolean, String> {
        val pattern = Pattern.compile("(?:\\w+\\.\\s*|\\d+\\.\\s*|\\d+\\s+)(.*)")

        val matcher = pattern.matcher(input)
        var hasAsterisk = false

        if (matcher.find()) {
            var restOfString = matcher.group(1)
            if ('*' in restOfString) {
                hasAsterisk = true
                restOfString = restOfString.replace("*", "")
            }
            return Pair(hasAsterisk, restOfString)
        } else {
            return Pair(false, input)
        }
    }


}