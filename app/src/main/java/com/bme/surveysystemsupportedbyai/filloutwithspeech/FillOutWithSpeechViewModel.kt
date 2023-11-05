package com.bme.surveysystemsupportedbyai.filloutwithspeech

import android.content.Context
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.google.gson.Gson
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class FillOutWithSpeechViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val surveysRepository: SurveysRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val survey = mutableStateOf(Survey())
    private var textToSpeechManager: TextToSpeechManager? = null
    val currentQuestionIndex = mutableIntStateOf(-1)
    private var speechToText: SpeechToText? = null
    var textState by mutableStateOf("Press the button to start")
    var buttonState by mutableStateOf("START")
    private var abc2 =
        listOf("A. ", "B. ", "C. ", "D. ", "E. ", "F. ", "G. ", "H. ", "I. ", "J. ", "K. ", "L. ")
    private var abc =
        listOf("AY. ", "B. ", "C. ", "D. ", "E. ", "F. ", "G. ", "H. ", "I. ", "J. ", "K. ", "L. ")
    private val apiKey = BuildConfig.OPENAI_API_KEY
    private var openai: OpenAI? = null
    private val answers = mutableListOf<Answer>()
    var fillOutState = mutableStateOf(FillOutState.Initial)
    private var prevFillOutState = mutableStateOf(FillOutState.Stop)
    private var currentAnswer: Answer? = null

    enum class FillOutState { Start, End, Confirmation, Question, Stop, Thinking, UserSpeaking, Sent, Initial }

    private var utteranceProgressListener: UtteranceProgressListener =
        object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {
            }

            override fun onDone(utteranceId: String) {
                if (utteranceId == "intro" || utteranceId.contains("answer") || utteranceId.contains(
                        "_end"
                    )
                ) {
                    if(fillOutState.value!= FillOutState.Stop) {
                        textState = ""
                        prevFillOutState.value = fillOutState.value
                        fillOutState.value = FillOutState.UserSpeaking
                        send(AppAction.StartRecord)
                    }
                }
                else textToSpeechManager?.nextItem()
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String) {
            }
        }


    init {
        val surveyId = savedStateHandle.get<String>(SURVEY_ID)
        if (surveyId != null) {
            viewModelScope.launch {
                survey.value = surveysRepository.getSurvey(surveyId.idFromParameter()) ?: Survey()
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
        openai = OpenAI(
            token = apiKey, timeout = Timeout(socket = 90.seconds)
        )
    }

    fun getCurrentQuestion(): Question {
        val index = currentQuestionIndex.intValue
        return if (index >= 0) survey.value.questions.getOrNull(index) ?: Question()
        else Question()
    }

    private fun moveToNextQuestion() {
        if (currentQuestionIndex.intValue <= survey.value.questions.size - 1) {
            currentQuestionIndex.intValue++
        }
    }

    private fun recordingEnded(text: String) {
        fillOutState.value = prevFillOutState.value
        when (fillOutState.value) {
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
        textState = ""
        prevFillOutState.value = fillOutState.value
        fillOutState.value = FillOutState.Start
        textToSpeechManager?.speak(
            "Hi! We are going to fill out the survey you received. The title of the survey is ${survey.value.title}. Can we start?",
            "intro"
        )
    }


    fun onButtonClick() {
        when (buttonState) {
            "START" -> {
                readSurvey()
                buttonState = "PAUSE"
            }
            "RESUME" -> {
                if (currentQuestionIndex.intValue == -1) readSurvey()
                else {
                    when (prevFillOutState.value) {
                        FillOutState.Question -> readNextQuestion(false)
                        FillOutState.Confirmation -> confirmAnswer(
                            currentAnswer
                        )
                        FillOutState.End -> {
                            onEnding()
                        }
                        else -> {}
                    }
                }
                buttonState = "PAUSE"
            }
            "PAUSE" -> {
                prevFillOutState.value = fillOutState.value
                fillOutState.value = FillOutState.Stop
                buttonState="RESUME"
            }
        }
    }

    private suspend fun sendChatCompletionRequestWithRetry(
        request: ChatCompletionRequest, timeout: Duration, maxRetries: Int
    ): ChatCompletion? {
        var retries = 0

        suspend fun doRequest(): ChatCompletion? {
            return withTimeoutOrNull(timeout) {
                openai?.chatCompletion(request)
            }
        }

        while (retries <= maxRetries) {
            try {
                val completion = doRequest()

                if (completion != null) {
                    return completion
                } else {
                    Log.d("openai", "Retrying request (attempt $retries)...")
                    textToSpeechManager?.speak("I am processing the answer. Please wait", "proc")
                    retries++
                    delay(100)
                }
            } catch (e: OpenAIServerException) {
                Log.e("OpenAI", "OpenAI Server Exception: ${e.message}")
                retries++
            } catch (e: Exception) {
                Log.e("OpenAI", "An unexpected exception occurred: ${e.message}")
                retries++
            }
        }
        return null
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
            fillOutState.value = FillOutState.Thinking
            var response: String?

            viewModelScope.launch {
                val timeout = 1.minutes
                val maxRetries = 3
                val completion =
                    sendChatCompletionRequestWithRetry(chatCompletionRequest, timeout, maxRetries)

                if (completion != null) {
                    response = completion.choices.first().message.content
                    fillOutState.value = FillOutState.Question
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
                } else {
                    textToSpeechManager?.speak("Sorry, I am currently unavailable.", "err")
                }
            }}
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
        textState = ""
        if (moveToNext) moveToNextQuestion()
        if (currentQuestionIndex.intValue == survey.value.questions.size) {
            onEnding()
            return
        }
        prevFillOutState.value = fillOutState.value
        fillOutState.value = FillOutState.Question
        val question = getCurrentQuestion()
        textToSpeechManager?.speak("Question ${currentQuestionIndex.intValue + 1}.", "num")
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
            "question${currentQuestionIndex}_intro"
        )
        textToSpeechManager?.speak(
            "${question.text} ",
            "question${currentQuestionIndex}_intro"
        )
        readOptions(question)
    }

    private fun readOptions(question: Question) {

        if (question.type == "short_answer") textToSpeechManager?.speak(
            " ", "question${currentQuestionIndex}_answer"
        )
        if (question.type == "multiple_choice") {
            textToSpeechManager?.speak(
                "The options are: ", "question${currentQuestionIndex}_options"
            )
            for (optionIndex in question.options.indices) {
                val option = question.options[optionIndex]
                textToSpeechManager?.speak(
                    "${abc[optionIndex]}: $option",
                    "question${currentQuestionIndex}_option${optionIndex}"
                )
            }
            textToSpeechManager?.speak(
                "Remember, you can only choose one. What is your answer? ",
                "question${currentQuestionIndex}_answer"
            )
        }
        if (question.type == "checkbox") {
            textToSpeechManager?.speak(
                "The options are: ", "question${currentQuestionIndex}_options"
            )
            for (optionIndex in question.options.indices) {
                val option = question.options[optionIndex]
                textToSpeechManager?.speak(
                    "${abc[optionIndex]}: $option",
                    "question${currentQuestionIndex}_option${optionIndex}"
                )
            }
            textToSpeechManager?.speak(
                "You can choose multiple responses. What is your answer? ",
                "question${currentQuestionIndex}_answer"
            )
        }
    }

    private fun onInactivity() {
        textToSpeechManager?.speak(
            "You were inactive. If you want to resume, click the resume button. ", "inactivity"
        )
        fillOutState.value = FillOutState.Stop
        buttonState = "RESUME"
    }

    private fun confirmAnswer(answer: Answer?) {
        prevFillOutState.value = fillOutState.value
        fillOutState.value = FillOutState.Confirmation
        if (answer != null) {
            val message = "Your answer is: ${
                if (answer.response.first()
                        .isNotEmpty()
                ) answer.response.joinToString(", ") else "Nothing "
            }. " + "Is this right? "
            textToSpeechManager?.speak(message, "yesno_answer")
        }
    }

    private fun processYesNoAnswer(text: String, answer: Answer?, end: Boolean =false, start:Boolean = false) {
        if (answer == null) return
        val system =
            "I asked a user a yes or no question. Based on their answer, did they say yes or no? You have to say just \"yes\" or \"no\". "
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"), messages = listOf(
                ChatMessage(
                    role = ChatRole.System, content = system
                ), ChatMessage(
                    role = ChatRole.User, content = text
                )
            ), temperature = 0.6
        )
        var response: String?
        fillOutState.value = FillOutState.Thinking
        viewModelScope.launch {
            val timeout = 1.minutes // 1 minute timeout
            val maxRetries = 3 // Maximum number of retries
            val completion =
                sendChatCompletionRequestWithRetry(chatCompletionRequest, timeout, maxRetries)
            if (completion != null) {
                response = completion.choices.first().message.content
                fillOutState.value = FillOutState.Question
                when (response) {
                    null -> {}
                    ("no") -> {
                        if(start) {
                            prevFillOutState.value = FillOutState.Start
                            fillOutState.value = FillOutState.Stop
                            textToSpeechManager?.speak(
                                "Okay, let me know if you want to fill out the survey.", "no_start"
                            )
                        }
                        else if (end) textToSpeechManager?.speak(
                            "Okay, let me know if you want to send it.", "no_send"
                        )
                        else {
                            textToSpeechManager?.speak("Okay, I repeat the question. ", "repeat")
                            readNextQuestion(false)
                        }
                    }

                    "yes" -> {
                        if(start){
                            readNextQuestion()
                        }
                        else if (end) {
                            textToSpeechManager?.speak("Okay, I am sending it.", "sending")
                            fillOutState.value= FillOutState.Sent
                            sendFilledOutSurvey()

                        } else {
                            textToSpeechManager?.speak("Answer saved. ", "saved")
                            delay(100)
                            answers.add(answer)

                            readNextQuestion()
                        }
                    }
                }
            } else {
                textToSpeechManager?.speak("Sorry, I am currently unavailable.", "err")
            }
        }
    }

    private fun generatePrompt(question: Question): String {
        var system = ""
        when (question.type) {
            "multiple_choice" -> system =
                "Understand the answer. Provide the user's response to a survey question without the initial letters (A. (= first), B. (= second), C. (= third), D...) from the options. If the user selects more than one option, return \"too_many_options\". Else if the question is NOT required and the user doesn't want to respond, return {\"response\": [\"\"]} . If the question is REQUIRED  and the answer's meaning does not match any of the options, return \"no_response\". Note that sometimes the answer starts with \"my answer is..\", which can be cut off. Required questions must have answers. if the answer is \"too_many_options\" or \"no_response\" return that. Else return the answer in JSON format, as follows: {\"response\": [\"Option Text\"]} to match the class structure Answer(var response: List<String). It can not contain anything else than the given option texts."

            "checkbox" -> system =
                "Understand the answer. Provide the user's response to a survey question without the initial letters (A. , B. , C. , D.) from the options. The option texts have to remain exactly the same. The user can respond with the letter of the option as well, then use the option text as response.  If the question is NOT required and the user doesn't want to respond, return {\"response\": [\"\"]}. If the user's response is not in the options, return \"no_response\". Required questions must have answers. if the answer is \"no_response\" return that. Else return the answer or answers in JSON format, as follows: {\"response\": [\"Option Text1\",\"OptionText2\"]} to match the class structure Answer(var response: List<String). It can not contain anything else than the given option texts."

            "short_answer" -> system =
                "Understand the answer. Provide the user's response to a survey question. If the question is NOT required and the user doesn't want to answer, return {\"response\": [\"\"]}. Required questions must have answers. Note that sometimes the answer starts with \"my answer is..\", which can be cut off. Return the answer or answers in JSON format, as follows: {\"response\": [\"answer\"]} to match the class structure Answer(var response: List<String)."
        }
        return system
    }

    private fun onEnding() {
        prevFillOutState.value = fillOutState.value
        fillOutState.value = FillOutState.End
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
                    textState = ""
                }
            }

            is AppAction.Update -> {
                textState += action.text
            }

        }
    }

    private fun sendFilledOutSurvey() {
        val surveyResponse = SurveyResponse(
            surveyId = survey.value.id, surveyTitle = survey.value.title, answers = answers
        )
        viewModelScope.launch {
            surveysRepository.fillOutSurvey(surveyResponse)
            textToSpeechManager?.speak("Survey response sent!", "sending_done")
        }
    }

    sealed class AppAction {
        object StartRecord : AppAction()
        object EndRecord : AppAction()
        data class Update(val text: String) : AppAction()
    }
}