package io.github.taetae98coding.diary.domain.sync.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.manager.SyncManager
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class RequestSyncUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    @param:Provided
    private val syncManager: SyncManager,
) {
    public suspend operator fun invoke(type: SyncType) {
        when (val account = getAccountUseCase().first().getOrThrow()) {
            is Account.Guest -> Unit
            is Account.User -> syncManager.requestSync(account.accountId, type)
        }
    }
}
