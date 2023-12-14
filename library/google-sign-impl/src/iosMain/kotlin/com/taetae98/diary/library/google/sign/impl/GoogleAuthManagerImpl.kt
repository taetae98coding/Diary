package com.taetae98.diary.library.google.sign.impl

import cocoapods.GoogleSignIn.GIDConfiguration
import cocoapods.GoogleSignIn.GIDSignIn
import cocoapods.GoogleSignIn.GIDSignInResult
import com.taetae98.diary.library.google.sign.api.GoogleAuthManager
import com.taetae98.diary.library.google.sign.api.GoogleCredential
import kotlin.coroutines.resume
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UIKit.UIViewController

@ExperimentalForeignApi
public class GoogleAuthManagerImpl(
    private val uiViewController: UIViewController,
) : GoogleAuthManager {

    init {
        runCatching {
            GIDSignIn.sharedInstance.setConfiguration(
                GIDConfiguration(
                    clientID = BuildKonfig.GOOGLE_CLIENT_ID,
                    serverClientID = BuildKonfig.GOOGLE_SERVER_CLIENT_ID,
                )
            )
        }
    }

    override suspend fun signIn(): GoogleCredential? {
        return suspendCancellableCoroutine {
            runCatching {
                GIDSignIn.sharedInstance.signInWithPresentingViewController(
                    presentingViewController = uiViewController,
                    completion = { result, _ ->
                        it.onSignInComplete(result)
                    }
                )
            }
        }
    }

    private fun CancellableContinuation<GoogleCredential?>.onSignInComplete(result: GIDSignInResult?) {
        if (result != null) {
            val idToken = result.user.idToken?.tokenString

            if (idToken == null) {
                resume(null)
            } else {
                resume(GoogleCredential(idToken, null))
            }
        } else {
            resume(null)
        }
    }
}