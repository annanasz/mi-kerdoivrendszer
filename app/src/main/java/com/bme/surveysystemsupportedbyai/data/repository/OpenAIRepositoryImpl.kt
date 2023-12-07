package com.bme.surveysystemsupportedbyai.data.repository

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.exception.OpenAIServerException
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.bme.surveysystemsupportedbyai.BuildConfig
import com.bme.surveysystemsupportedbyai.domain.repository.OpenAIRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class OpenAIRepositoryImpl @Inject constructor(
    private var openai: OpenAI,
    @ApplicationContext private val context: Context
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
        var response = "Service Unavailable"
        if (completion != null) {
            response = completion.choices.first().message.content.toString()
        }
        return response
    }

    private suspend fun sendChatCompletionRequestWithRetry(
        request: ChatCompletionRequest,
        timeout: Duration,
        maxRetries: Int,
        retryingCallBack: () -> Unit
    ): ChatCompletion? {
        var retries = 0

        suspend fun doRequest(): ChatCompletion? {
            return withTimeoutOrNull(timeout) {
                openai.chatCompletion(request)
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

    override fun resetApiKey(){
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        var apiKey= BuildConfig.OPENAI_API_KEY
        if(apiKey == "null")
            apiKey = sharedPreferences.getString("openai_apikey","none").toString()
        openai = OpenAI(
            token = apiKey, timeout = Timeout(socket = 90.seconds)
        )
    }

    override suspend fun isApiKeyCorrect(): Boolean {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"), messages = listOf(
                ChatMessage(
                    role = ChatRole.System, content = "say something random"
                ), ChatMessage(
                    role = ChatRole.User, content = ""
                )
            ), temperature = 0.6
        )
        val timeout = 20.seconds
        val maxRetries = 2
        val completion =
            sendChatCompletionRequestWithRetry(
                chatCompletionRequest,
                timeout,
                maxRetries
            ) { }
        if(completion!=null)
            return true
        return false
    }
}