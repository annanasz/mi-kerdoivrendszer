package com.bme.surveysystemsupportedbyai.di

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.bme.surveysystemsupportedbyai.BuildConfig
import com.bme.surveysystemsupportedbyai.data.repository.AuthRepositoryImpl
import com.bme.surveysystemsupportedbyai.data.repository.OpenAIRepositoryImpl
import com.bme.surveysystemsupportedbyai.data.repository.SurveysRepositoryImpl
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.OpenAIRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {
    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(
        auth = Firebase.auth
    )
    @Provides
    fun provideSurveysRepository():SurveysRepository = SurveysRepositoryImpl(
        firestore = Firebase.firestore,
        auth = provideAuthRepository()
    )
    @Provides
    fun provideOpenAi(@ApplicationContext context: Context): OpenAIRepository {
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
        val openai = OpenAI(
            token = apiKey, timeout = Timeout(socket = 90.seconds)
        )
        return OpenAIRepositoryImpl(openai, context)
    }
}