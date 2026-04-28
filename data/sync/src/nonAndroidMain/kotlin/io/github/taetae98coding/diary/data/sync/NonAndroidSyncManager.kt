package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.model.sync.SyncStatus
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.data.sync.di.SyncCoroutineScope
import io.github.taetae98coding.diary.domain.sync.manager.SyncManager
import kotlin.uuid.Uuid
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
internal class NonAndroidSyncManager(
    @param:Provided
    @param:SyncCoroutineScope
    private val coroutineScope: CoroutineScope,
    private val synchronizer: Synchronizer,
) : SyncManager {
    private var job: Job? = null
    private val mutex = Mutex()
    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)
    override val syncStatus = _syncStatus.asStateFlow()

    override fun requestSync(
        accountId: Uuid,
        type: SyncType,
    ) {
        job?.cancel()
        job = coroutineScope.launch {
            mutex.withLock {
                _syncStatus.value = SyncStatus.Syncing(type)
                runCatching { synchronizer.sync(accountId) }
                    .onSuccess { _syncStatus.value = SyncStatus.Idle }
                    .onFailure { throwable ->
                        if (throwable is CancellationException) {
                            throw throwable
                        } else {
                            throwable.printStackTrace()
                            _syncStatus.value = SyncStatus.Failed
                        }
                    }
            }
        }
    }
}
