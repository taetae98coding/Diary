package io.github.taetae98coding.diary.domain.buddy.usecase

import io.github.taetae98coding.diary.core.model.account.Account.Guest
import io.github.taetae98coding.diary.core.model.buddy.Buddy
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindBuddyUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val repository: BuddyRepository,
) {
    public operator fun invoke(email: String?): Flow<Result<List<Buddy>>> {
        if (email.isNullOrBlank()) return flowOf(Result.success(emptyList()))

        return getAccountUseCase().mapLatest { it.getOrThrow() }
            .flatMapLatest { account ->
                if (account is Guest) {
                    flowOf(emptyList())
                } else {
                    repository.findBuddyByEmail(email)
                }
            }
            .mapLatest { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}
