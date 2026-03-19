package io.github.taetae98coding.diary.domain.sync.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.repository.SyncRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class SyncUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    private val syncRepository: SyncRepository,
) {
    public suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            when (val account = getAccountUseCase().first().getOrThrow()) {
                is Account.User -> syncRepository.sync(account.accountId)
                is Account.Guest -> Unit
            }
        }
    }
}
