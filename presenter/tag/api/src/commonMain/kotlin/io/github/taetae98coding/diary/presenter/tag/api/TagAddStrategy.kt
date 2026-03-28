package io.github.taetae98coding.diary.presenter.tag.api

import io.github.taetae98coding.diary.core.model.tag.TagDetail

public interface TagAddStrategy {
    public suspend fun add(detail: TagDetail): Result<Unit>
}
