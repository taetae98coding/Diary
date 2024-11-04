package io.github.taetae98coding.diary

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import io.github.taetae98coding.diary.app.manager.BackupManager
import io.github.taetae98coding.diary.app.manager.FetchManager
import org.koin.android.ext.android.get

public class DiaryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initBackupManager()
        initFetchManager()
    }

    private fun initBackupManager() {
        val appLifecycleOwner = get<LifecycleOwner>()
        val backupManager = get<BackupManager>()

        backupManager.attach(appLifecycleOwner)
    }

    private fun initFetchManager() {
        val appLifecycleOwner = get<LifecycleOwner>()
        val fetchManager = get<FetchManager>()

        fetchManager.attach(appLifecycleOwner)
    }
}
