@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.library.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

public fun <T, R> Flow<Collection<T>>.mapCollectionLatest(transform: suspend (T) -> R): Flow<List<R>> {
    return mapLatest { collection -> collection.map { transform(it) } }
}
