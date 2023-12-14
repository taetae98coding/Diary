package com.taetae98.diary.library.google.sign.impl

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.taetae98.diary.library.google.sign.api.GoogleAuthManager
import com.taetae98.diary.library.google.sign.api.GoogleCredential

public class GoogleAuthManagerImpl(
    private val context: Context,
) : GoogleAuthManager {
    override suspend fun signIn(): GoogleCredential? {
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

    private suspend fun execute(request: GetCredentialRequest): GoogleCredential? {
        return runCatching {
            val response = CredentialManager.create(context).getCredential(context, request)
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(response.credential.data)

            GoogleCredential(
                idToken = googleIdTokenCredential.idToken,
                accessToken = null,
            )
        }.getOrNull()
    }
}