package com.bme.surveysystemsupportedbyai.filloutwithspeech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

interface SpeechToText {
    val text: StateFlow<String>
    fun start()
    fun stop()
}

class RealSpeechToText(context: Context, private val onInactivity: ()->Unit, recordingEnded: (String)->Unit) : SpeechToText {
    override val text = MutableStateFlow("")
    private var done=false

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
        setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                done=false
            }
            override fun onBeginningOfSpeech() {
                done=false
            }
            override fun onRmsChanged(p0: Float) = Unit
            override fun onBufferReceived(p0: ByteArray?) = Unit
            override fun onEndOfSpeech() {
                done=true
            }
            override fun onResults(results: Bundle?) {
                done=true
                recordingEnded(text.value)
            }
            override fun onEvent(p0: Int, p1: Bundle?){
                Log.d("TAG", "onEvent() type: $p0")
                p1?.let {
                    for (key in p1.keySet()) Log.d(
                        "TAG",
                        (p1.get(key) as String)
                    )
                }

            }

            override fun onPartialResults(results: Bundle?) {
                val partial = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.getOrNull(0) ?: ""

                Log.d("Speech Recognizer", "onPartialResult: $partial")
                text.value = partial
            }

            override fun onError(error: Int) {
                val message = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio"
                    SpeechRecognizer.ERROR_CANNOT_CHECK_SUPPORT -> "Cannot Check Support"
                    SpeechRecognizer.ERROR_CLIENT -> "Client"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient Permissions"
                    SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED -> "Language Not Supported"
                    SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> "Language Unavailable"
                    SpeechRecognizer.ERROR_NETWORK -> "Network"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network Timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> {
                        "No Match"
                        onInactivity()
                    }
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server Error"
                    SpeechRecognizer.ERROR_SERVER_DISCONNECTED -> "Server Disconnected"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech Timeout"
                    SpeechRecognizer.ERROR_TOO_MANY_REQUESTS -> "Too Many Requests"
                    else -> "Unknown"
                }
                Log.e("Speech Recognizer", "STT Error: $message")
            }
        })
    }
    private val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale("en")
        )
        putExtra(
            RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
            2000
        )
    }


    override fun start() {
        speechRecognizer.startListening(intent)
    }

    override fun stop() {
        speechRecognizer.stopListening()
    }
}