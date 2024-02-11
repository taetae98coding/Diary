package com.taetae98.diary.library.kotlin.ext

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
public fun<T, R> Flow<Collection<T>>.mapCollectionLatest(
    transform:  (value: T) -> R
): Flow<List<R>> {
    return mapLatest { it.map(transform) }
}
