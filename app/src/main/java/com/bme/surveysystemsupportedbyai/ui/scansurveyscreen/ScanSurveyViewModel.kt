package com.bme.surveysystemsupportedbyai.ui.scansurveyscreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bme.surveysystemsupportedbyai.core.Constants.IMAGE_PROC_ERROR_MESSAGE
import com.bme.surveysystemsupportedbyai.core.Constants.TEXT_RECOGNITION_ERROR_MESSAGE
import com.bme.surveysystemsupportedbyai.domain.model.Question
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.ui.mysurveys.SURVEY_ID
import com.bme.surveysystemsupportedbyai.navigation.Screen
import com.google.firebase.ml.common.FirebaseMLException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ScanSurveyViewModel @Inject constructor(
    private val surveysRepository: SurveysRepository,
    savedStateHandle: SavedStateHandle,
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(ScanSurveyUiState())
    val uiState = _uiState.asStateFlow()
    private var savedSurveyId: String? = ""
    private var newSurvey = true

    init {
        val surveyId =
            savedStateHandle.get<String>(com.bme.surveysystemsupportedbyai.navigation.SURVEY_ID)
        if (!surveyId.isNullOrEmpty()) {
            savedSurveyId = surveyId
            newSurvey = false
        }
    }

    private fun capitalizeFirstLetter(input: String): String {
        if (input.isEmpty()) {
            return input
        }

        val lowercase = input.lowercase()
        return lowercase.substring(0, 1).uppercase() + lowercase.substring(1)
    }
    private fun processTextResult(result: Text,  uppercase:Boolean=false,callback: (String?) -> Unit) {
        var title = ""
        val questions = mutableListOf<Question>()
        var actualQuestion: Question? = null
        for (block in result.textBlocks) {
            for (line in block.lines) {
                val lineText = line.text
                if (newSurvey && title == "") title = if(uppercase) {
                    capitalizeFirstLetter(lineText)
                } else {
                    lineText
                }
                else {
                    if (lineText[0] != 'o' && lineText[0] != 'O' && lineText[0] != 'X' && lineText[0] != 'x') {
                        if (actualQuestion != null) {
                            questions.add(actualQuestion)
                            if (actualQuestion.type == "") actualQuestion.type =
                                "short_answer"
                        }
                        actualQuestion = Question()
                        val lineTextCut = extractRestOfStringAndCheckAsterisk(lineText)
                        actualQuestion.isRequired = lineTextCut.first
                        actualQuestion.text = if(uppercase) {
                            capitalizeFirstLetter(lineTextCut.second)
                        } else{
                            lineTextCut.second
                        }
                    } else if (lineText[0] == 'o' || lineText[0] == 'O') {
                        actualQuestion?.type = "multiple_choice"
                        var lineTextCut = removeLeadingOAndSpace(lineText)
                        val list: MutableList<String>? = actualQuestion?.options?.toMutableList()
                        if(uppercase)
                            lineTextCut = capitalizeFirstLetter(lineTextCut)
                        list?.add(lineTextCut)
                        if (list != null) {
                            actualQuestion?.options = list
                        }
                    } else if (lineText[0] != 'X' || lineText[0] != 'x') {
                        actualQuestion?.type = "checkbox"
                        var lineTextCut = removeLeadingXAndSpace(lineText)
                        if(uppercase)
                            lineTextCut = capitalizeFirstLetter(lineTextCut)
                        val list: MutableList<String>? = actualQuestion?.options?.toMutableList()
                        list?.add(lineTextCut)
                        if (list != null) {
                            actualQuestion?.options = list
                        }
                    }
                }
            }
        }

        if (actualQuestion != null) {
            if (actualQuestion.type == "")
                actualQuestion.type = "short_answer"
            questions.add(actualQuestion)
        }
        if (savedSurveyId.isNullOrEmpty()) {
            val survey = Survey(
                title = title,
                questions = questions,
            )
            viewModelScope.launch {
                savedSurveyId = surveysRepository.saveSurvey(survey)
                callback(savedSurveyId)
            }
        } else {
            viewModelScope.launch {
                val surveyToUpdate = surveysRepository.getSurvey(savedSurveyId!!)
                if (surveyToUpdate != null) {
                    val originalQuestions = mutableListOf<Question>()
                    originalQuestions.addAll(surveyToUpdate.questions)
                    originalQuestions.addAll(questions)
                    val updatedSurvey = surveyToUpdate.copy(questions = originalQuestions)
                    surveysRepository.updateSurvey(updatedSurvey)
                    callback(savedSurveyId)
                }
            }
        }
    }

    fun capturePhoto(
        context: Context, cameraController: LifecycleCameraController
    ) {
        val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

        cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val correctedBitmap =
                    rotateBitmapIfNeeded(image.toBitmap(), image.imageInfo.rotationDegrees)
                image.close()
                _uiState.update { uiState -> uiState.copy(isDialogOpen = true, capturedPhoto = correctedBitmap) }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraContent", "Error capturing image", exception)
            }
        })
    }

    fun rotateBitmapIfNeeded(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
        return if (rotationDegrees == 0) {
            bitmap
        } else {
            val matrix = Matrix()
            matrix.postRotate(rotationDegrees.toFloat())
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }
    fun processImage(openScreen: (String) -> Unit){
        _uiState.update { uiState-> uiState.copy(loading = true) }
        if(_uiState.value.capturedPhoto != null) {
            val inputImage = InputImage.fromBitmap(_uiState.value.capturedPhoto!!, 0)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    processTextResult(visionText){
                        result -> if(result!=null) openEditScreen(result, openScreen)
                    }
                }
                .addOnFailureListener { e ->
                    var errorMessage: String = IMAGE_PROC_ERROR_MESSAGE
                    if (e is FirebaseMLException && e.code == FirebaseMLException.UNAVAILABLE) {
                        errorMessage = TEXT_RECOGNITION_ERROR_MESSAGE
                    }
                    _uiState.update { it.copy(errorMessage = errorMessage) }
                }
        }
    }


    fun openEditScreen(surveyId: String, openScreen: (String) -> Unit) {
        openScreen("${Screen.SurveyEditScreen.route}?$SURVEY_ID={${surveyId}}")
    }

    private fun removeLeadingOAndSpace(input: String): String {
        return input.replace(regex = "^[oO]\\s*".toRegex(), replacement = "")
    }

    private fun removeLeadingXAndSpace(input: String): String {
        return input.replace(regex = "^[xX]\\s*".toRegex(), replacement = "")
    }

    private fun extractRestOfStringAndCheckAsterisk(input: String): Pair<Boolean, String> {
        val pattern = Pattern.compile("^\\d*[.,]?\\s*(.*)")
        val matcher = pattern.matcher(input)
        var hasAsterisk = false

        return if (matcher.find()) {
            var restOfString = matcher.group(1)
            if (restOfString != null) {
                if ('*' in restOfString) {
                    hasAsterisk = true
                    restOfString = restOfString.replace("*", "")
                }
            }
            Pair(hasAsterisk, restOfString)
        } else {
            Pair(false, input)
        }
    }

    fun onDismissClick (){
        _uiState.update { uiState-> uiState.copy(isDialogOpen = false, capturedPhoto = null) }
    }

    fun onUpperCaseClicked(isChecked:Boolean){
        _uiState.update { uiState-> uiState.copy(uppercase = isChecked) }
    }
}

data class ScanSurveyUiState(
    val isDialogOpen: Boolean = false,
    val uppercase: Boolean = false,
    val loading: Boolean = false,
    val capturedPhoto: Bitmap? = null,
    val errorMessage: String = ""
)