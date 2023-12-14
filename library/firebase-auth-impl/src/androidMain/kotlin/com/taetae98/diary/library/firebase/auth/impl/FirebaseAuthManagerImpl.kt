package com.taetae98.diary.library.firebase.auth.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager
import kotlinx.coroutines.tasks.await

public class FirebaseAuthManagerImpl : FirebaseAuthManager {
    override suspend fun signIn(idToken: String) {
        val googleCredential = GoogleAuthProvider.getCredential(idToken, null)

        FirebaseAuth.getInstance().signInWithCredential(googleCredential).await()
    }
}