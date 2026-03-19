package io.github.taetae98coding.diary.feature.login.home

internal sealed class LoginEffect {
    data object None : LoginEffect()
    data object Finish : LoginEffect()
    data object UnknownError : LoginEffect()
}
