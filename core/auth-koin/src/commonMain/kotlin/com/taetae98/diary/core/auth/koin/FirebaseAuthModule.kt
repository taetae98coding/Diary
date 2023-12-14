package com.taetae98.diary.core.auth.koin

import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
internal expect class FirebaseAuthModule() {
    @Factory
    fun providesFirebaseAuthManager(): FirebaseAuthManager
}