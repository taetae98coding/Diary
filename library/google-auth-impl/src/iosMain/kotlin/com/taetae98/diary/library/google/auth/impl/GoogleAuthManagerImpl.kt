package com.taetae98.diary.library.google.auth.impl

import cocoapods.GoogleSignIn.GIDConfiguration
import cocoapods.GoogleSignIn.GIDSignIn
import com.taetae98.diary.library.google.auth.api.GoogleAuthManager
import com.taetae98.diary.library.google.oauth.BuildKonfig
import kotlinx.cinterop.ExperimentalForeignApi
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

    override suspend fun signIn() {
        return suspendCancellableCoroutine {
            runCatching {
                GIDSignIn.sharedInstance.signInWithPresentingViewController(
                    presentingViewController = uiViewController,
                    completion = { result, error ->
                        println("${result?.user?.userID} -> $error")
                    }
                )
            }
        }
    }
}