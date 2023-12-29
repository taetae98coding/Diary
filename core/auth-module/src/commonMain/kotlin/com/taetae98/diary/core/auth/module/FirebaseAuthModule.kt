package com.taetae98.diary.core.auth.module

import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
internal class FirebaseAuthModule {
    @Factory
    fun providesFirebaseAuthManager(): FirebaseAuthManager {
        return getFirebaseAuthManager()
    }
}