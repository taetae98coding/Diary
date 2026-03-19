package io.github.taetae98coding.diary.library.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

public inline fun <reified T, R> Collection<Flow<T>>.combine(crossinline transform: suspend (Array<T>) -> R): Flow<R> {
    return combine(flows = toTypedArray(), transform = transform)
}
