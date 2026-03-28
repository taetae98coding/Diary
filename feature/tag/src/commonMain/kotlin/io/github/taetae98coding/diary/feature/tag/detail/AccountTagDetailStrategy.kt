package io.github.taetae98coding.diary.feature.tag.detail

import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.domain.tag.usecase.DeleteTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.FinishTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.GetTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.RestartTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.UpdateTagUseCase
import io.github.taetae98coding.diary.presenter.tag.api.TagDetailStrategy
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountTagDetailStrategy(
    private val getTagUseCase: GetTagUseCase,
    private val updateTagUseCase: UpdateTagUseCase,
    private val finishTagUseCase: FinishTagUseCase,
    private val restartTagUseCase: RestartTagUseCase,
    private val deleteTagUseCase: DeleteTagUseCase,
) : TagDetailStrategy {
    override fun get(tagId: Uuid): Flow<Result<Tag?>> {
        return getTagUseCase(tagId)
    }

    override suspend fun update(
        tagId: Uuid,
        detail: TagDetail,
    ): Result<Unit> {
        return updateTagUseCase(tagId, detail)
    }

    override suspend fun finish(tagId: Uuid): Result<Unit> {
        return finishTagUseCase(tagId)
    }

    override suspend fun restart(tagId: Uuid): Result<Unit> {
        return restartTagUseCase(tagId)
    }

    override suspend fun delete(tagId: Uuid): Result<Unit> {
        return deleteTagUseCase(tagId)
    }
}
