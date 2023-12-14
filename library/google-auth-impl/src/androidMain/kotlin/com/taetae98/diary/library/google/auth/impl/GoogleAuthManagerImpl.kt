package com.taetae98.diary.library.google.auth.impl

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.taetae98.diary.library.google.auth.api.GoogleAuthManager

public class GoogleAuthManagerImpl(
    private val context: Context,
) : GoogleAuthManager {
    override suspend fun signIn() {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        execute(request)
    }

    private suspend fun execute(request: GetCredentialRequest) {
        runCatching {
            CredentialManager.create(context).getCredential(context, request)
        }.onSuccess {
            println("PASSZ : $it")
        }.onFailure {
            println("PASSZ : $it")
        }
    }
}