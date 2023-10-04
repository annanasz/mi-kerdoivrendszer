package com.bme.surveysystemsupportedbyai.di

import com.bme.surveysystemsupportedbyai.data.repository.AuthRepositoryImpl
import com.bme.surveysystemsupportedbyai.data.repository.SurveysRepositoryImpl
import com.bme.surveysystemsupportedbyai.domain.repository.AuthRepository
import com.bme.surveysystemsupportedbyai.domain.repository.SurveysRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

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
}