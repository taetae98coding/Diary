package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpdateMemoUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val repository: MemoRepository,
) {
    public suspend operator fun invoke(memoId: String?, detail: MemoDetail): Result<Unit> {
        return runCatching {
            if (memoId.isNullOrBlank()) return@runCatching

            val memo = repository.find(memoId).first() ?: return@runCatching
            val account = getAccountUseCase().first().getOrThrow()
            val validDetail = detail.copy(title = detail.title.ifBlank { memo.detail.title })

            if (memo.detail != validDetail) {
                repository.update(account.uid, memoId, validDetail)
            }
        }
    }
}
