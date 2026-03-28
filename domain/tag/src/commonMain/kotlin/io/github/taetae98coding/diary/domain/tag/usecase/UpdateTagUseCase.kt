package io.github.taetae98coding.diary.domain.tag.usecase

import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.github.taetae98coding.diary.domain.tag.repository.AccountTagRepository
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpdateTagUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    private val tagRepository: TagRepository,
    private val accountTagRepository: AccountTagRepository,
    private val requestSyncUseCase: RequestSyncUseCase,
) {
    public suspend operator fun invoke(
        tagId: Uuid,
        detail: TagDetail,
    ): Result<Unit> {
        return runCatching {
            val account = getAccountUseCase().first().getOrThrow()
            val detail = detail.copy(
                title = detail.title.ifBlank { requireNotNull(tagRepository.get(tagId).first()).detail.title },
            )

            accountTagRepository.updateDetail(
                tagId = tagId,
                detail = detail,
            )

            requestSyncUseCase()
        }
    }
}
