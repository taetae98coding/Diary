package com.taetae98.diary.core.auth.module

import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager
import com.taetae98.diary.library.firebase.auth.impl.FirebaseAuthManagerImpl

internal actual fun FirebaseAuthModule.getFirebaseAuthManager(): FirebaseAuthManager {
    return FirebaseAuthManagerImpl()
}