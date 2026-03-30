package io.github.taetae98coding.diary.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// TODO Koin annotations
internal class SyncWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params),
    KoinComponent {
    private val getAccountUseCase: GetAccountUseCase by inject()
    private val synchronizer: Synchronizer by inject()

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
