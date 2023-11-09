package com.bme.surveysystemsupportedbyai.data.repository

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.exception.OpenAIServerException
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.bme.surveysystemsupportedbyai.domain.repository.OpenAIRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class OpenAIRepositoryImpl @Inject constructor(
    private val openai: OpenAI
) : OpenAIRepository {
    override suspend fun sendQuestion(
        system: String,
        message: String,
        retryingCallBack: () -> Unit
    ):String {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"), messages = listOf(
                ChatMessage(
                    role = ChatRole.System, content = system
                ), ChatMessage(
                    role = ChatRole.User, content = message
                )
            ), temperature = 0.6
        )
        val timeout = 30.seconds
        val maxRetries = 3
        val completion =
            sendChatCompletionRequestWithRetry(
                chatCompletionRequest,
                timeout,
                maxRetries,
                retryingCallBack
            )
        var response: String = "Service Unavailable"
        if (completion != null) {
            response = completion.choices.first().message.content.toString()
        }
        return response
    }

    suspend fun sendChatCompletionRequestWithRetry(
        request: ChatCompletionRequest,
        timeout: Duration,
        maxRetries: Int,
        retryingCallBack: () -> Unit
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
                    retryingCallBack()
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
}