@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.domain.sync.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.manager.SyncManager
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class RequestSyncUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    @param:Provided
    private val syncManager: SyncManager,
) {
    public suspend operator fun invoke(type: SyncType) {
        val account = withTimeoutOrNull(5.seconds) {
            getAccountUseCase().mapLatest { it.getOrThrow() }
                .first { it.isAuthorized }
        } ?: getAccountUseCase().first().getOrThrow()

        when (account) {
            is Account.Guest -> Unit
            is Account.User -> syncManager.requestSync(account.accountId, type)
        }
    }
}
