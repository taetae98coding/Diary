@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.library.paging.common

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

public fun <T : Any, R : Any> Flow<PagingData<T>>.mapPagingLatest(transform: suspend (T) -> R): Flow<PagingData<R>> {
    return mapLatest { pagingData -> pagingData.map(transform) }
}
