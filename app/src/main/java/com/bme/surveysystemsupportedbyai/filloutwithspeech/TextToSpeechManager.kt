package com.bme.surveysystemsupportedbyai.filloutwithspeech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener

import java.util.*

class TextToSpeechManager(context: Context, private val onInitCallback: () -> Unit) :
    TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech = TextToSpeech(context, this)

    private var utteranceQueue: Queue<Pair<String, String>> = LinkedList()

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
        utteranceQueue.offer(text to taskId)
        if (utteranceQueue.size >= 1 && !textToSpeech.isSpeaking) {
            val nextUtterance = utteranceQueue.poll()
            if (nextUtterance != null) {
                textToSpeech.speak(
                    nextUtterance.first, TextToSpeech.QUEUE_ADD, null, nextUtterance.second
                )
            }
        }
    }
    fun shutdown() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    fun nextItem() {
        if (!utteranceQueue.isEmpty()) {
            val nextUtterance = utteranceQueue.poll()
            if (nextUtterance != null) {
                textToSpeech.speak(
                    nextUtterance.first, TextToSpeech.QUEUE_ADD, null, nextUtterance.second
                )
            }
        }
    }

    fun setOnUtteranceProgressListener(utteranceListener: UtteranceProgressListener) {
        textToSpeech.setOnUtteranceProgressListener(utteranceListener)
    }
}