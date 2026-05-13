@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.data.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import io.github.taetae98coding.diary.core.model.sync.SyncStatus
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.sync.manager.SyncManager
import kotlin.time.Clock
import kotlin.uuid.Uuid
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Single

@Single
internal class AndroidSyncManager(
    private val context: Context,
    private val clock: Clock,
    getAccountUseCase: GetAccountUseCase,
) : SyncManager {
    override val syncStatus = getAccountUseCase().flatMapLatest { accountResult ->
        accountResult.fold(
            onSuccess = { account ->
                WorkManager.getInstance(context).getWorkInfosForUniqueWorkFlow("SYNC_${account.accountId}")
                    .mapLatest { workInfos ->
                        val last = workInfos.maxByOrNull { info ->
                            info.tags.find { it.startsWith("timestamp") }
                                ?.removePrefix("timestamp")
                                ?.toLongOrNull()
                                ?: 0L
                        } ?: return@mapLatest SyncStatus.Idle

                        val syncType = SyncType.entries.find { last.tags.contains(it.name) } ?: SyncType.Background

                        when (last.state) {
                            WorkInfo.State.RUNNING, WorkInfo.State.ENQUEUED -> SyncStatus.Syncing(syncType)
                            WorkInfo.State.FAILED -> SyncStatus.Failed(syncType)
                            else -> SyncStatus.Idle
                        }
                    }
            },
            onFailure = { flowOf(SyncStatus.Idle) },
        )
    }

    override fun requestSync(
        accountId: Uuid,
        type: SyncType,
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf(SyncWorker.ACCOUNT_ID to accountId.toString()))
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("timestamp${clock.now().toEpochMilliseconds()}")
            .addTag(type.name)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            uniqueWorkName = "SYNC_$accountId",
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = request,
        )
    }
}
