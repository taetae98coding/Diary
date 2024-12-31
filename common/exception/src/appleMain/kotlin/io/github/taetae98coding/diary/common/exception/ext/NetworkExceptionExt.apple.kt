package io.github.taetae98coding.diary.common.exception.ext

import io.ktor.client.engine.darwin.DarwinHttpRequestException

public actual fun Throwable.isNetworkException(): Boolean {
	if (this is DarwinHttpRequestException) {
		if (origin.code == -1004L || origin.code == -1009L) {
			return true
		}
	}

	return false
}
