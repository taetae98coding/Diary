package io.github.taetae98coding.diary.core.google.credentials.impl

import GoogleSignIn.GIDSignIn
import GoogleSignIn.GIDSignInResult
import GoogleSignIn.kGIDSignInErrorCodeCanceled
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentials
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsException
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsManager
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsNotExistException
import io.github.taetae98coding.diary.core.google.credentials.api.GoogleCredentialsUserCancelException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
public class GoogleCredentialsManagerImpl(private val uiViewController: UIViewController) : GoogleCredentialsManager {
    override suspend fun get(): GoogleCredentials {
        return suspendCancellableCoroutine { continuation ->
            GIDSignIn.sharedInstance.signInWithPresentingViewController(uiViewController) { result: GIDSignInResult?, error: NSError? ->
                if (result != null) {
                    val idToken = result.user.idToken?.tokenString
                    if (idToken == null) {
                        continuation.resumeWithException(GoogleCredentialsNotExistException())
                        return@signInWithPresentingViewController
                    }

                    continuation.resume(GoogleCredentials.IdToken(idToken))
                    return@signInWithPresentingViewController
                }

                if (error != null) {
                    continuation.resumeWithException(error.toGoogleCredentialsException())
                    return@signInWithPresentingViewController
                }

                continuation.resumeWithException(GoogleCredentialsException())
            }
        }
    }

    private fun NSError.toGoogleCredentialsException(): GoogleCredentialsException {
        return when (code) {
            kGIDSignInErrorCodeCanceled -> GoogleCredentialsUserCancelException()
            else -> GoogleCredentialsException()
        }
    }
}
