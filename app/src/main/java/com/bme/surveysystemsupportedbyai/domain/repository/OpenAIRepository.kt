package com.bme.surveysystemsupportedbyai.domain.repository


interface OpenAIRepository {
    suspend fun sendQuestion(system: String, message: String, retryingCallBack: ()->Unit):String
}