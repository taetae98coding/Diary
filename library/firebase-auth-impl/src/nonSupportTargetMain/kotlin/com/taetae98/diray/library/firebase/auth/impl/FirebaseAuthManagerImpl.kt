package com.taetae98.diray.library.firebase.auth.impl

import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager

public class FirebaseAuthManagerImpl : FirebaseAuthManager {
    override suspend fun signIn(idToken: String): Unit = Unit
}