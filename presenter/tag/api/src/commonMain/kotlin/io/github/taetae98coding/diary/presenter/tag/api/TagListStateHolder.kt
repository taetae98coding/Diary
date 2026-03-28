package io.github.taetae98coding.diary.presenter.tag.api

import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.taetae98coding.diary.core.model.tag.Tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

public class TagListStateHolder(
    coroutineScope: CoroutineScope,
    strategy: TagListStrategy,
) {
    public val pagingData: Flow<PagingData<Tag>> = strategy.page()
        .mapLatest { it.getOrDefault(PagingData.empty()) }
        .cachedIn(coroutineScope)
}
