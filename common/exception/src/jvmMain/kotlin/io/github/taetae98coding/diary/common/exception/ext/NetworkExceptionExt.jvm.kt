package io.github.taetae98coding.diary.common.exception.ext

import java.net.ConnectException
import java.net.SocketTimeoutException

public actual fun Throwable.isNetworkException(): Boolean = this is ConnectException || this is SocketTimeoutException
