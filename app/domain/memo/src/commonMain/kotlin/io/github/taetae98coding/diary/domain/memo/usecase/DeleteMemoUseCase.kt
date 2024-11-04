package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class DeleteMemoUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val repository: MemoRepository,
) {
    public suspend operator fun invoke(memoId: String?): Result<Unit> {
        return runCatching {
            if (memoId.isNullOrBlank()) return@runCatching

            val account = getAccountUseCase().first().getOrThrow()
            repository.updateDelete(account.uid, memoId, true)
        }
    }
}
