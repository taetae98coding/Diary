package io.github.taetae98coding.diary.domain.backup.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.repository.MemoBackupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
public class PushMemoBackupQueueUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val backupUseCase: BackupUseCase,
    private val coroutineScope: CoroutineScope,
    private val repository: MemoBackupRepository,
) {
    public suspend operator fun invoke(memoId: String?): Result<Unit> {
        return runCatching {
            if (memoId.isNullOrBlank()) return@runCatching

            val account = getAccountUseCase().first().getOrThrow()
            if (account is Account.Member) {
                coroutineScope.launch {
                    repository.upsertBackupQueue(account.uid, memoId)
                    backupUseCase()
                }
            }
        }
    }
}
