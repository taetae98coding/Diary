package io.github.taetae98coding.diary.initializer

import androidx.lifecycle.LifecycleOwner
import io.github.taetae98coding.diary.app.manager.BackupManager
import org.koin.core.KoinApplication

internal fun initBackupManager(
    koinApplication: KoinApplication,
) {
    val appLifecycleOwner = koinApplication.koin.get<LifecycleOwner>()
    val backupManager = koinApplication.koin.get<BackupManager>()

    backupManager.attach(appLifecycleOwner)
}
