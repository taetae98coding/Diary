package io.github.taetae98coding.diary.domain.fetch.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.fetch.repository.FetchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FetchUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val repository: FetchRepository,
) {
    public suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            getAccountUseCase().mapLatest { it.getOrNull() }
                .collectLatest { account ->
                    if (account is Account.Member) {
                        runCatching { fetch(account.uid) }
                    }
                }
        }
    }

    private suspend fun fetch(uid: String) {
        repository.fetchMemo(uid)
    }
}
