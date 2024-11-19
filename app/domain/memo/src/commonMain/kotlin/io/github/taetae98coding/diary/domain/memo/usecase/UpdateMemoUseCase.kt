package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.repository.BackupRepository
import io.github.taetae98coding.diary.domain.backup.usecase.BackupUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
public class UpdateMemoUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val repository: MemoRepository,
    private val coroutineScope: CoroutineScope,
    private val backupRepository: BackupRepository,
    private val backupUseCase: BackupUseCase,
) {
    public suspend operator fun invoke(memoId: String?, detail: MemoDetail): Result<Unit> {
        return runCatching {
            if (memoId.isNullOrBlank()) return@runCatching

            val memo = repository.find(memoId).first() ?: return@runCatching
            val account = getAccountUseCase().first().getOrThrow()
            val validDetail = detail.copy(title = detail.title.ifBlank { memo.detail.title })

            if (memo.detail == validDetail) return@runCatching

            repository.update(memoId, validDetail)
            if (account is Account.Member) {
                coroutineScope.launch {
                    backupRepository.upsertMemoBackupQueue(account.uid, memoId)
                    backupUseCase()
                }
            }
        }
    }
}
