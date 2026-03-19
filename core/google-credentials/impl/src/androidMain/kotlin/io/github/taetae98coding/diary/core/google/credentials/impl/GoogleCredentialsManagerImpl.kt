package io.github.taetae98coding.diary.core.google.credentials.impl

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentials
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsException
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsManager
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsNotExistException
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsUserCancelException

public class GoogleCredentialsManagerImpl(
    private val clientId: String,
    private val context: Context,
) : GoogleCredentialsManager {
    override suspend fun get(): GoogleCredentials {
        return try {
            val credentialManager = CredentialManager.create(context)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(clientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val credential = GoogleIdTokenCredential.createFrom(result.credential.data)

            GoogleCredentials.IdToken(credential.idToken)
        } catch (exception: NoCredentialException) {
            throw GoogleCredentialsNotExistException(exception)
        } catch (exception: GetCredentialCancellationException) {
            throw GoogleCredentialsUserCancelException(exception)
        } catch (cause: Throwable) {
            throw GoogleCredentialsException(cause)
        }
    }
}
