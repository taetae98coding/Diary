package io.github.taetae98coding.diary.domain.backup.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.repository.BackupRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class BackupUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val repository: BackupRepository,
) {
    public suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            getAccountUseCase().mapLatest { it.getOrNull() }
                .flatMapLatest { account ->
                    if (account is Account.Member) {
                        repository.getUpdateFlow(account.uid)
                            .mapLatest { account.uid }
                    } else {
                        emptyFlow()
                    }
                }
                .collectLatest { uid ->
                    runCatching { backup(uid) }
                }
        }
    }

    private suspend fun backup(uid: String) {
        repository.backupMemo(uid)
    }
}
