package com.taetae98.diary.core.auth.koin

import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager
import com.taetae98.diary.library.firebase.auth.impl.FirebaseAuthManagerImpl
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
internal actual class FirebaseAuthModule {
    @Factory
    actual fun providesFirebaseAuthManager(): FirebaseAuthManager {
        return FirebaseAuthManagerImpl()
    }
}