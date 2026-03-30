package io.github.taetae98coding.diary.domain.sync.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.SyncManager
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class RequestSyncUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    private val syncManager: SyncManager,
) {
    public suspend operator fun invoke(type: SyncType) {
        when (val account = getAccountUseCase().first().getOrThrow()) {
            is Account.Guest -> Unit
            is Account.User -> syncManager.requestSync(account.accountId, type)
        }
    }
}
