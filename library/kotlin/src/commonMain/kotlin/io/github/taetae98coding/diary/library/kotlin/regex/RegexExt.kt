package io.github.taetae98coding.diary.library.kotlin.regex

public fun Regex.Companion.email(): Regex {
    return Regex("[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]+")
}
