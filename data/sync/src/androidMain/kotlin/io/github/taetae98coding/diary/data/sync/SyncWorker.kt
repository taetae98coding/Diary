package io.github.taetae98coding.diary.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import org.koin.android.annotation.KoinWorker

@KoinWorker
internal class SyncWorker(
    context: Context,
    params: WorkerParameters,
    private val getAccountUseCase: GetAccountUseCase,
    private val synchronizer: Synchronizer,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            val currentAccount = getAccountUseCase().first().getOrThrow()
            val accountId = Uuid.parse(requireNotNull(inputData.getString(ACCOUNT_ID)))

            if (currentAccount.accountId == accountId) {
                synchronizer.sync(accountId)
            }

            Result.success()
        } catch (_: Throwable) {
            Result.retry()
        }
    }

    companion object {
        const val ACCOUNT_ID = "accountId"
    }
}
