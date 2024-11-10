package io.github.taetae98coding.diary.domain.fetch.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.fetch.repository.FetchRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class FetchUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val repository: FetchRepository,
) {
    public suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            val account = getAccountUseCase().first().getOrThrow()
            if (account is Account.Member) {
                repository.fetchMemo(account.uid)
            }
        }
    }
}
