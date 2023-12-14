package com.taetae98.diary.library.google.auth.impl

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.taetae98.diary.library.google.auth.api.GoogleAuthManager

public class GoogleAuthManagerImpl(
    private val context: Context,
) : GoogleAuthManager {
    override suspend fun signIn(): String {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return execute(request)
    }

    private suspend fun execute(request: GetCredentialRequest): String {
        return runCatching {
            val credential = CredentialManager.create(context).getCredential(context, request).credential as CustomCredential
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            googleIdTokenCredential.idToken
        }.getOrDefault("")
    }
}