package io.github.taetae98coding.diary.data.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import io.github.taetae98coding.diary.core.model.sync.SyncStatus
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.SyncManager
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Single

@Single
internal class AndroidSyncManager(
    private val context: Context,
    getAccountUseCase: GetAccountUseCase,
) : SyncManager {
    override val syncStatus: Flow<SyncStatus> = getAccountUseCase().flatMapLatest { result ->
        result.fold(
            onSuccess = { account ->
                WorkManager.getInstance(context).getWorkInfosForUniqueWorkFlow("SYNC_${account.accountId}")
                    .mapLatest { workInfos ->
                        if (workInfos.any { it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED }) {
                            SyncStatus.Syncing
                        } else {
                            SyncStatus.Idle
                        }
                    }
            },
            onFailure = { flowOf(SyncStatus.Idle) },
        )
    }

    override fun requestSync(accountId: Uuid) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf(SyncWorker.ACCOUNT_ID to accountId.toString()))
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            uniqueWorkName = "SYNC_$accountId",
            existingWorkPolicy = ExistingWorkPolicy.KEEP,
            request = request,
        )
    }
}
