package com.taetae98.diary.core.auth.module

import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager

internal expect fun FirebaseAuthModule.getFirebaseAuthManager(): FirebaseAuthManager