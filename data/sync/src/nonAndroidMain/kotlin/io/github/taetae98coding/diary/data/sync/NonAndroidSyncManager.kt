package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.model.sync.SyncStatus
import io.github.taetae98coding.diary.data.sync.di.SyncCoroutineScope
import io.github.taetae98coding.diary.domain.sync.SyncManager
import kotlin.uuid.Uuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.annotation.Single

@Single
internal class NonAndroidSyncManager(
    @param:SyncCoroutineScope
    private val coroutineScope: CoroutineScope,
    private val synchronizer: Synchronizer,
) : SyncManager {
    private val mutex = Mutex()
    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)
    override val syncStatus = _syncStatus.asStateFlow()

    override fun requestSync(accountId: Uuid) {
        coroutineScope.launch {
            if (mutex.isLocked) {
                mutex.withLock { }
            } else {
                mutex.withLock {
                    _syncStatus.value = SyncStatus.Syncing
                    runCatching { synchronizer.sync(accountId) }
                        .onFailure { it.printStackTrace() }
                    _syncStatus.value = SyncStatus.Idle
                }
            }
        }
    }
}
