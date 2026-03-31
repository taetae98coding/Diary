package io.github.taetae98coding.diary.domain.tag.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.github.taetae98coding.diary.domain.tag.repository.AccountTagRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class DeleteTagUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    private val requestSyncUseCase: RequestSyncUseCase,
    @param:Provided
    private val accountTagRepository: AccountTagRepository,
) {
    public suspend operator fun invoke(tagId: Uuid): Result<Unit> {
        return runCatching {
            val account = getAccountUseCase().first().getOrThrow()

            accountTagRepository.updateDelete(tagId = tagId, isDeleted = true)

            requestSyncUseCase(SyncType.Background)
        }
    }
}
