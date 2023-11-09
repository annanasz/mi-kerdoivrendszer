package com.bme.surveysystemsupportedbyai.filloutwithspeech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.delay

import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class TextToSpeechManager(context: Context, private val onInitCallback: () -> Unit) :
    TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech = TextToSpeech(context, this)

    private var utteranceQueue: Queue<Pair<String, String>> = ConcurrentLinkedQueue()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val locale = Locale.ENGLISH
            textToSpeech.language = locale
            textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String) {
                }

                override fun onDone(utteranceId: String) {
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String) {
                }
            })
            onInitCallback()
        }
    }

    fun speak(text: String, taskId: String) {
        Log.e("TTS", "I entered the function: $taskId")
        synchronized(this) {
            utteranceQueue.offer(text to taskId)
            Log.e("TTS", "TTS queue added: $text")
            if (utteranceQueue.size >= 1) {
                val nextUtterance = utteranceQueue.poll()
                if (nextUtterance != null) {
                    textToSpeech.speak(
                        nextUtterance.first, TextToSpeech.QUEUE_ADD, null, nextUtterance.second
                    )
                    Log.e("TTS", "TTS spoke: $text")
                }
            }
        }
    }
    fun stopSpeaking() {
        synchronized(this) {
            textToSpeech.stop()
            Log.e("TTS", "TTS queue cleared, was: ${utteranceQueue.joinToString(" ,")}")
            utteranceQueue.clear()
        }
    }

    fun shutdown() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    fun nextItem() {
        synchronized(this) {
            if (!utteranceQueue.isEmpty()) {
                val nextUtterance = utteranceQueue.poll()
                if (nextUtterance != null) {
                    textToSpeech.speak(
                        nextUtterance.first, TextToSpeech.QUEUE_ADD, null, nextUtterance.second
                    )
                }
            }
        }
    }

    fun setOnUtteranceProgressListener(utteranceListener: UtteranceProgressListener) {
        textToSpeech.setOnUtteranceProgressListener(utteranceListener)
    }
}