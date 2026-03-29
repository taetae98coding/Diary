package io.github.taetae98coding.diary.feature.tag.add

import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.domain.tag.usecase.AddTagUseCase
import io.github.taetae98coding.diary.presenter.tag.api.TagAddStrategy
import org.koin.core.annotation.Factory

@Factory
internal class AccountTagAddStrategy(private val addTagUseCase: AddTagUseCase) : TagAddStrategy {
    override suspend fun add(detail: TagDetail): Result<Unit> {
        return addTagUseCase(detail)
    }
}
