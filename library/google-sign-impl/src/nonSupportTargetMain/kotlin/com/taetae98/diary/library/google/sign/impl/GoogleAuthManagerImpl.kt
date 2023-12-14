package com.taetae98.diary.library.google.sign.impl

import com.taetae98.diary.library.google.sign.api.GoogleAuthManager
import com.taetae98.diary.library.google.sign.api.GoogleCredential

public class GoogleAuthManagerImpl : GoogleAuthManager {
    override suspend fun signIn(): GoogleCredential? {
        return null
    }
}