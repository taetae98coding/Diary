package com.taetae98.diary.library.paging

import app.cash.paging.Pager
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow

public fun <K : Any, T : Any, R : Any> Pager<K, T>.mapPaging(
    transform: suspend (T) -> R,
): Flow<PagingData<R>> {
    return flow.mapPaging(transform)
}