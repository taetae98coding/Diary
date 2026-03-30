package io.github.taetae98coding.diary.domain.tag.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.model.tag.TagTitleBlankException
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.github.taetae98coding.diary.domain.tag.repository.AccountTagRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class AddTagUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    private val accountTagRepository: AccountTagRepository,
    private val requestSyncUseCase: RequestSyncUseCase,
) {
    public suspend operator fun invoke(detail: TagDetail): Result<Unit> {
        return runCatching {
            if (detail.title.isBlank()) throw TagTitleBlankException()

            val account = getAccountUseCase().first().getOrThrow()

            accountTagRepository.add(
                accountId = account.accountId,
                detail = detail,
            )

            requestSyncUseCase(SyncType.Background)
        }
    }
}
