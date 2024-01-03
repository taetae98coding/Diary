package com.taetae98.diary.data.remote.impl

internal actual fun getProxyUrl(): String? {
    if (!BuildConfig.DEBUG) return null

    val protocol = when {
        !System.getProperty("http.proxyHost").isNullOrEmpty() -> "http"
        !System.getProperty("https.proxyHost").isNullOrEmpty() -> "https"
        else -> return null
    }

    val host = System.getProperty("$protocol.proxyHost")
    val port = System.getProperty("$protocol.proxyPort")

    return "$protocol://$host:$port/"
}