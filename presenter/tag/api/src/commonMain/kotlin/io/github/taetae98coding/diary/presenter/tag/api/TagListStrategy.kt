package io.github.taetae98coding.diary.presenter.tag.api

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.tag.Tag
import kotlinx.coroutines.flow.Flow

public interface TagListStrategy {
    public fun page(): Flow<Result<PagingData<Tag>>>
}
