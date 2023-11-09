package com.bme.surveysystemsupportedbyai.filloutwithspeech

import android.content.Context
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.exception.OpenAIServerException
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.bme.surveysystemsupportedbyai.BuildConfig
import com.bme.surveysystemsupportedbyai.domain.model.Question
import com.bme.surveysystemsupportedbyai.domain.model.Survey
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.bme.surveysystemsupportedbyai.navigation.SURVEY_ID
import com.bme.surveysystemsupportedbyai.surveyDetails.idFromParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.aallam.openai.client.OpenAI
import com.bme.surveysystemsupportedbyai.domain.model.Answer
import com.bme.surveysystemsupportedbyai.domain.model.SurveyResponse
import com.bme.surveysystemsupportedbyai.domain.repository.OpenAIRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.internal.wait
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class FillOutWithSpeechViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val surveysRepository: SurveysRepository,
    private val openAIRepository: OpenAIRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private var textToSpeechManager: TextToSpeechManager? = null
    private var speechToText: SpeechToText? = null
    private var abc2 =
        listOf("A. ", "B. ", "C. ", "D. ", "E. ", "F. ", "G. ", "H. ", "I. ", "J. ", "K. ", "L. ")
    private var abc =
        listOf("AY. ", "B. ", "C. ", "D. ", "E. ", "F. ", "G. ", "H. ", "I. ", "J. ", "K. ", "L. ")
    private val answers = mutableListOf<Answer>()
    private var prevFillOutState = mutableStateOf(FillOutState.Stop)
    private var currentAnswer: Answer? = null
    private var paused = false

    enum class FillOutState { Start, End, Confirmation, Question, Stop, Thinking, UserSpeaking, Sent, Initial }

    private val _uiState = MutableStateFlow(FillOutWithSpeechUiState())
    val uiState: StateFlow<FillOutWithSpeechUiState> = _uiState.asStateFlow()

    private var utteranceProgressListener: UtteranceProgressListener =
        object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {
            }

            override fun onDone(utteranceId: String) {
                if (utteranceId == "intro" || utteranceId.contains("answer") || utteranceId.contains(
                        "_end"
                    )
                ) {
                    if (_uiState.value.fillOutState != FillOutState.Stop) {
                        _uiState.update { it.copy(textState = "") }
                        prevFillOutState.value = _uiState.value.fillOutState
                        _uiState.update { it.copy(fillOutState = FillOutState.UserSpeaking) }
                        send(AppAction.StartRecord)
                    }
                } else {
                    if (_uiState.value.fillOutState != FillOutState.Stop) {
                        textToSpeechManager?.nextItem()
                    }
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String) {
                Log.e("TTS", "Error in speech with utteranceId: $utteranceId")
            }
        }


    init {
        val surveyId = savedStateHandle.get<String>(SURVEY_ID)
        if (surveyId != null) {
            viewModelScope.launch {
                surveysRepository.getSurvey(surveyId.idFromParameter())?.let { survey ->
                    _uiState.update { it.copy(survey = survey) }
                }
            }
        }
        textToSpeechManager =
            TextToSpeechManager(context, onInitCallback = {
                textToSpeechManager?.setOnUtteranceProgressListener(
                    utteranceProgressListener
                )
            })

        speechToText =
            RealSpeechToText(context, { onInactivity() }) { text -> recordingEnded(text) }
        viewModelScope.launch {
            with(speechToText) {
                this!!.text.collect { result ->
                    send(AppAction.Update(result))
                }
            }
        }
    }

    private fun getCurrentQuestion(): Question {
        val index = _uiState.value.currentQuestionIndex
        return if (index >= 0) _uiState.value.survey.questions.getOrNull(index) ?: Question()
        else Question()
    }

    private fun moveToNextQuestion() {
        val index = _uiState.value.currentQuestionIndex
        if (index <= _uiState.value.survey.questions.size - 1) {
            _uiState.value = _uiState.value.copy(currentQuestionIndex = index + 1)
        }
    }

    private fun recordingEnded(text: String) {
        _uiState.update { it.copy(fillOutState = prevFillOutState.value) }
        when (_uiState.value.fillOutState) {
            FillOutState.Confirmation -> {
                processYesNoAnswer(text, currentAnswer)
            }

            FillOutState.Start -> {
                processYesNoAnswer(text, Answer(response = listOf(text)), start = true)
            }

            FillOutState.Question -> {
                processAnswer(text)
            }

            FillOutState.End -> {
                processYesNoAnswer(text, currentAnswer, end = true)
            }

            else -> {}
        }
    }


    private fun readSurvey() {
        prevFillOutState.value = _uiState.value.fillOutState
        _uiState.update { it.copy(textState = "", fillOutState = FillOutState.Start) }
        textToSpeechManager?.speak(
            "Hi! We are going to fill out the survey you received. The title of the survey is ${_uiState.value.survey.title}. Can we start?",
            "intro"
        )
    }


    fun onButtonClick() {
        when (_uiState.value.buttonState) {
            "START" -> {
                readSurvey()
                paused = false
                _uiState.update { it.copy(buttonState = "PAUSE") }
            }

            "RESUME" -> {
                paused = false
                textToSpeechManager?.stopSpeaking()
                if (_uiState.value.currentQuestionIndex == -1) readSurvey()
                else {
                    when (prevFillOutState.value) {
                        FillOutState.Question -> readNextQuestion(false)
                        FillOutState.Confirmation -> confirmAnswer(
                            currentAnswer
                        )

                        FillOutState.End -> {
                            onEnding()
                        }

                        FillOutState.Thinking -> readNextQuestion(false)
                        FillOutState.UserSpeaking -> readNextQuestion(false)
                        else -> {

                        }
                    }
                }
                _uiState.update { it.copy(buttonState = "PAUSE") }
            }

            "PAUSE" -> {
                paused = true
                prevFillOutState.value = _uiState.value.fillOutState
                _uiState.update {
                    it.copy(
                        buttonState = "RESUME",
                        fillOutState = FillOutState.Stop
                    )
                }
                textToSpeechManager?.stopSpeaking()
                speechToText?.stop()
            }
        }
    }

    private fun processAnswer(text: String) {
        val question = getCurrentQuestion()

        if (question.id.isNotEmpty()) {
            val system = generatePrompt(getCurrentQuestion())
            val message =
                "${if (question.isRequired) "required" else "not required"} question: ${question.text}, type=${question.type}${
                    if (question.type != "short_answer") ", options: ${
                        abc2.zip(question.options)
                            .joinToString(", ") { (letter, option) -> "$letter$option" }
                    }." else "."
                }\n answer: \"$text\"."
            val chatCompletionRequest = ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"), messages = listOf(
                    ChatMessage(
                        role = ChatRole.System, content = system
                    ), ChatMessage(
                        role = ChatRole.User, content = message
                    )
                ), temperature = 0.6
            )
            _uiState.update { it.copy(fillOutState = FillOutState.Thinking) }
            var response: String?

            viewModelScope.launch {
                response = openAIRepository.sendQuestion(system, message) {
                    textToSpeechManager?.speak(
                        "I am processing the answer. Please wait. ",
                        "proc"
                    )
                }
                if (response != "Service Unavailable") {
                    if (!paused) {
                        _uiState.update { it.copy(fillOutState = FillOutState.Question) }
                        when (response) {
                            null -> {}
                            "no_response" -> {
                                noResponse()
                            }

                            "too_many_options" -> {
                                tooManyOptions()
                            }

                            else -> {
                                val gson = Gson()
                                val answer = gson.fromJson(response, Answer::class.java)
                                answer.type = question.type
                                answer.questionId = question.id
                                currentAnswer = answer
                                confirmAnswer(answer)
                            }
                        }
                    }
                } else {
                    if (!paused)
                        textToSpeechManager?.speak("Sorry, I am currently unavailable.", "err")
                }
            }
        }
    }

    private fun noResponse() {
        var message = "Sorry, I can't figure out your answer. Let's try again! "
        if (getCurrentQuestion().isRequired) message =
            "I didn't understand your answer. The question is required, so you must respond! "
        textToSpeechManager?.speak(message, "no_response")
        readNextQuestion(false)
    }

    private fun tooManyOptions() {
        val message = "You provided too many answers. Choose only one. "
        textToSpeechManager?.speak(message, "too_many_options")
        readNextQuestion(false)
    }

    private fun readNextQuestion(moveToNext: Boolean = true) {
        Log.e("TTS", "readnext")
        _uiState.update { it.copy(textState = "") }
        if (moveToNext) moveToNextQuestion()
        if (_uiState.value.currentQuestionIndex == _uiState.value.survey.questions.size) {
            onEnding()
            return
        }
        prevFillOutState.value = _uiState.value.fillOutState
        _uiState.update { it.copy(fillOutState = FillOutState.Question) }
        val question = getCurrentQuestion()
        textToSpeechManager?.speak("Question ${_uiState.value.currentQuestionIndex + 1}.", "num")
        val type: String = when (question.type) {

            "multiple_choice" -> {
                "You have to choose only one from the options. "
            }

            "checkbox" -> {
                "You can choose multiple answers from the options. "
            }

            else -> {
                "You just have to answer."
            }
        }
        var required = "required"
        if (!question.isRequired) required = "optional"
        textToSpeechManager?.speak(
            "${type}. It is $required to answer. ",
            "question${_uiState.value.currentQuestionIndex}_intro"
        )
        textToSpeechManager?.speak(
            "${question.text} ",
            "question${_uiState.value.currentQuestionIndex}_intro"
        )
        readOptions(question)
    }

    private fun readOptions(question: Question) {

        if (question.type == "short_answer") textToSpeechManager?.speak(
            " ", "question${_uiState.value.currentQuestionIndex}_answer"
        )
        if (question.type == "multiple_choice") {
            textToSpeechManager?.speak(
                "The options are: ", "question${_uiState.value.currentQuestionIndex}_options"
            )
            for (optionIndex in question.options.indices) {
                val option = question.options[optionIndex]
                textToSpeechManager?.speak(
                    "${abc[optionIndex]}: $option",
                    "question${_uiState.value.currentQuestionIndex}_option${optionIndex}"
                )
            }
            textToSpeechManager?.speak(
                "Remember, you can only choose one. What is your answer? ",
                "question${_uiState.value.currentQuestionIndex}_answer"
            )
        }
        if (question.type == "checkbox") {
            textToSpeechManager?.speak(
                "The options are: ", "question${_uiState.value.currentQuestionIndex}_options"
            )
            for (optionIndex in question.options.indices) {
                val option = question.options[optionIndex]
                textToSpeechManager?.speak(
                    "${abc[optionIndex]}: $option",
                    "question${_uiState.value.currentQuestionIndex}_option${optionIndex}"
                )
            }
            textToSpeechManager?.speak(
                "You can choose multiple responses. What is your answer? ",
                "question${_uiState.value.currentQuestionIndex}_answer"
            )
        }
    }

    private fun onInactivity() {
        paused = true
        textToSpeechManager?.speak(
            "You were inactive. If you want to resume, click the resume button. ", "inactivity"
        )
        _uiState.update { it.copy(buttonState = "RESUME", fillOutState = FillOutState.Stop) }
    }

    private fun confirmAnswer(answer: Answer?) {
        prevFillOutState.value = _uiState.value.fillOutState
        _uiState.update { it.copy(fillOutState = FillOutState.Confirmation) }
        if (answer != null) {
            val message = "Your answer is: ${
                if (answer.response.first()
                        .isNotEmpty()
                ) answer.response.joinToString(", ") else "Nothing "
            }. " + "Is this right? "
            textToSpeechManager?.speak(message, "yesno_answer")
        }
    }

    private fun processYesNoAnswer(
        text: String,
        answer: Answer?,
        end: Boolean = false,
        start: Boolean = false
    ) {
        if (answer == null) return
        val system =
            "I asked a user a yes or no question. Based on their answer, did they say yes or no? You have to say just \"yes\" or \"no\". "
        var response: String?
        _uiState.update { it.copy(fillOutState = FillOutState.Thinking) }
        viewModelScope.launch {
            response = openAIRepository.sendQuestion(system, text) {
                textToSpeechManager?.speak(
                    "I am processing the answer. Please wait. ",
                    "proc"
                )
            }
            if (response != "Service Unavailable") {
                if (!paused) {
                    _uiState.update { it.copy(fillOutState = FillOutState.Question) }
                    when (response) {
                        null -> {
                            textToSpeechManager?.speak(
                                "ona.",
                                "no_start"
                            )
                        }

                        ("no") -> {
                            if (start) {
                                prevFillOutState.value = FillOutState.Start
                                _uiState.update { it.copy(fillOutState = FillOutState.Stop) }
                                textToSpeechManager?.speak(
                                    "Okay, let me know if you want to fill out the survey.",
                                    "no_start"
                                )
                            } else if (end) textToSpeechManager?.speak(
                                "Okay, let me know if you want to send it.", "no_send"
                            )
                            else {
                                textToSpeechManager?.speak(
                                    "Okay, I repeat the question. ",
                                    "repeat"
                                )
                                readNextQuestion(false)
                            }
                        }

                        "yes" -> {
                            if (start) {
                                readNextQuestion()
                            } else if (end) {
                                textToSpeechManager?.speak("Okay, I am sending it.", "sending")
                                _uiState.update { it.copy(fillOutState = FillOutState.Sent) }
                                sendFilledOutSurvey()

                            } else {
                                textToSpeechManager?.speak("Answer saved. ", "saved")
                                delay(200)
                                answers.add(answer)
                                readNextQuestion()
                            }
                        }
                        else -> {
                            Log.e("TTS", "else respomse")
                        }
                    }
                }
            } else {
                _uiState.update { it.copy(fillOutState = FillOutState.Question) }
                textToSpeechManager?.speak("Sorry, I am currently unavailable.", "err")
            }
        }
    }

    private fun generatePrompt(question: Question): String {
        var system = ""
        when (question.type) {
            "multiple_choice" -> system =
                "Provide the user's response to the question without the initial letters (A. (= first), B. (= second), C. (= third), D...) from the options. Never provide more than one answers! If the user selects more than one option, return \"too_many_options\". Else if the question is NOT required and the user doesn't want to respond, return {\"response\": [\"\"]} . If the question is REQUIRED  and the answer's meaning does not match any of the options or the user doesn't want to respond, return \"no_response\". Required questions must have answers, so you can't return {\"response\": [\"\"]}. If there is not a single answer, return either \"no_response\", or \"too_many_options\"  if the answer is \"too_many_options\" or \"no_response\" return that. Else return the answer in JSON format, as follows: {\"response\": [\"Option Text\"]} to match the class structure Answer(var response: List<String>). It can NOT contain anything else than the given option texts. Few examples:\n" +
                        "1. example:\n" +
                        "user: \"not required question: \"Are you a boy?\"; options: A. \"Yes\", B. \"No\".\n" +
                        "answer: \"b b b\".\n" +
                        "assistant:{\"response\": [\"No\"]}\n" +
                        "2. example:\n" +
                        "user: \"required question: \"Who wrote Romeo and Juliet?\"; options: A. \"Charles Dickens\", B. \"William Shakespeare\", C. \"Jane Austen\", D. \"F. Scott Fitzgerald\".\n" +
                        "answer: \"I think it was A no it was Shakespeare\".\n" +
                        "assistant:{\"response\": [ \"William Shakespeare\"]}\n" +
                        "3. example:\n" +
                        "user: \"required question: \"Are you hungry?\"; options: A. \"Yes\", B. \"No\".\n" +
                        "answer: \"yes\".\n" +
                        "assistant:{\"response\": [ \"Yes\"]}\n" +
                        "4. example:\n" +
                        "user: \"not required question: \"Are you hungry?\"; options: A. \"Yes\", B. \"No\".\n" +
                        "answer: \"yes but I don't want to tell you\".\n" +
                        "assistant:{\"response\": [\"\"]}\n" +
                        "5. example:\n" +
                        "user: \"not required question: \"Are you a boy?\"; options: A. \"Yes\", B. \"No\".\n" +
                        "answer: \"Yes and no.\".\n" +
                        "assistant:\"too_many_options\"\n" +
                        "6. example:\n" +
                        "user: \"required question: \"Are you hungry?\"; options: A. \"Yes\", B. \"No\".\n" +
                        "answer: \"I don't respond\".\n" +
                        "assistant:\"no_response\"\n" +
                        "7. example:\n" +
                        "user: \"required question: \"Who wrote Romeo and Juliet?\"; options: A. \"Charles Dickens\", B. \"William Shakespeare\", C. \"Jane Austen\", D. \"F. Scott Fitzgerald\".\n" +
                        "answer: \"The third one\".\n" +
                        "assistant:{\"response\": [ \"Jane Austen\"]}"

            "checkbox" -> system =
                "Provide the user's response to a question without the initial letters (A. , B. , C. , D.) from the options. The option texts have to remain exactly the same. The user can respond with the letter of the option as well, then use the option text as response. If the question is NOT required and the user doesn't want to respond, return {\"response\": [\"\"]}. If the user's response is not in the options, return \"no_response\". If the question is REQUIRED  and the answer's meaning does not match any of the options, return \"no_response\". Required questions must have an answer, so you can't return {\"response\": [\"\"]} in that case. If the answer is \"no_response\" return that. Else return the answer or answers in JSON format, as follows: {\"response\": [\"Option Text1\",\"Option Text2\"]} to match the class structure Answer(var response: List<String). It can not contain anything else than the given option texts. Few examples:\n" +
                        "1. example:\n" +
                        "user: \"not required question: \"What are your hobbies?\"; options: A. \"Running\", B. \"Gym\", C. \"Skydiving\".\n" +
                        "answer: \"b b b\".\n" +
                        "assistant:{\"response\": [\"Gym\"]}\n" +
                        "2. example:\n" +
                        "user: \"not required question: \"What are your hobbies?\"; options: A. \"Running\", B. \"Gym\", C. \"Skydiving\".\n" +
                        "answer: \"hmm I like tennis\".\n" +
                        "assistant: \"no_response\"\n" +
                        "3. example:\n" +
                        "user: \"not required question: \"What is your favorite drink?\"; options: A. \"Coffee\", B. \"Matcha\", C. \"Tea\", D. \"Beer\".\n" +
                        "answer: \"skip this question\".\n" +
                        "assistant:{\"response\": [\"\"]}\n" +
                        "4. example:\n" +
                        "user: \"required question: \"What foods do you like?\"; options: A. \"Pizza\", B. \"Pasta\", C. \"Steak\".\n" +
                        "answer: \"Pasta but I don't want to answer\".\n" +
                        "assistant:\"no_response\"\n" +
                        "5. example:\n" +
                        "user: \"required question: \"What is your favorite drink?\"; options: A. \"Coffee\", B. \"Matcha\", C. \"Tea\", D. \"Beer\".\n" +
                        "answer: \"coffee and  matcha\".\n" +
                        "assistant:{\"response\": [\"Coffee\", \"Matcha\"]}\n" +
                        "6. example:\n" +
                        "user: \"required question: \"Are you hungry?\"; options: A. \"Yes\", B. \"No\".\n" +
                        "answer: \"I don't respond\".\n" +
                        "assistant:\"no_response\""

            "short_answer" -> system =
                "Provide the user's response to a survey question. If the question is NOT required and the user doesn't want to answer, return {\"response\": [\"\"]}. If the question is REQUIRED  and the user doesn't want to respond, return \"no_response\". Required questions must have an answer, so you can't return {\"response\": [\"\"]} in that case. Note that sometimes the answer starts with \"my answer is..\", which can be cut off. If the user responds in a way that half of the question is in it, cut that off too. Return the answer or answers in JSON format, as follows: {\"response\": [\"answer\"]} to match the class structure Answer(var response: List<String). Few examples:\n" +
                        "1. example:\n" +
                        "user: \"not required question: \"What are your hobbies?\"\n" +
                        "answer: \"my hobby is running or no it is swimming\".\n" +
                        "assistant:{\"response\": [\"Swimming\"]}\n" +
                        "2. example:\n" +
                        "user: \"not required question: \"What is your name?\"\n" +
                        "answer: \"my name is carol\".\n" +
                        "assistant:{\"response\": [\"Carol\"]}\n" +
                        "3. example:\n" +
                        "user: \"not required question: \"How old are you?\"\n" +
                        "answer: \"my answer is twenty\".\n" +
                        "assistant:{\"response\": [\"20\"]}\n" +
                        "4. example:\n" +
                        "user: \"not required question: \"How old are you?\"\n" +
                        "answer: \"I don't want to answer\".\n" +
                        "assistant:{\"response\": [\"\"]}\n" +
                        "5. example:\n" +
                        "user: \"not required question: \"Who is your favorite singer?\"\n" +
                        "answer: \"My favorite singer is Miley Cyrus but I don't want to tell you\".\n" +
                        "assistant:{\"response\": [\"\"]}\n" +
                        "6. example:\n" +
                        "user: \"required question: \"What is your favorite color?\"\n" +
                        "answer: \"I don't want to answer\".\n" +
                        "assistant:\"no_response\""
        }
        return system
    }

    private fun onEnding() {
        prevFillOutState.value = _uiState.value.fillOutState
        _uiState.update { it.copy(fillOutState = FillOutState.End) }
        textToSpeechManager?.speak(
            "We got to the end of the survey. Can I send your answer to the sender?", "survey_end"
        )
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeechManager?.shutdown()
    }

    fun send(action: AppAction) {
        when (action) {
            AppAction.StartRecord -> {
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        speechToText?.start()
                    }
                }
            }

            AppAction.EndRecord -> {
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        speechToText?.stop()
                    }
                }
                viewModelScope.launch {
                    delay(3000)
                    //textState = ""
                    _uiState.update { it.copy(textState = "") }
                }
            }

            is AppAction.Update -> {
                val textState = _uiState.value.textState + action.text
                _uiState.update { it.copy(textState = textState) }
            }

            else -> {}
        }
    }

    private fun sendFilledOutSurvey() {
        val surveyResponse = SurveyResponse(
            surveyId = _uiState.value.survey.id,
            surveyTitle = _uiState.value.survey.title,
            answers = answers
        )
        viewModelScope.launch {
            surveysRepository.fillOutSurvey(surveyResponse)
            textToSpeechManager?.speak("Survey response sent!", "sending_done")
        }
    }

    fun onScreenDisposed() {
        textToSpeechManager?.stopSpeaking()
        speechToText?.stop()
    }

    sealed class AppAction {
        object StartRecord : AppAction()
        object EndRecord : AppAction()
        data class Update(val text: String) : AppAction()
    }
}

data class FillOutWithSpeechUiState(
    val survey: Survey = Survey(),
    val currentQuestionIndex: Int = -1,
    val textState: String = "",
    val buttonState: String = "START",
    val fillOutState: FillOutWithSpeechViewModel.FillOutState = FillOutWithSpeechViewModel.FillOutState.Initial
)

val FillOutWithSpeechUiState.currentQuestion: Question get() = if (currentQuestionIndex >= 0 && currentQuestionIndex < survey.questions.size) survey.questions[currentQuestionIndex] else Question()