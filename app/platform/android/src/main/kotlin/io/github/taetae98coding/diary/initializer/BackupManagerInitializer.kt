package io.github.taetae98coding.diary.initializer

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.startup.Initializer
import io.github.taetae98coding.diary.app.manager.BackupManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

public class BackupManagerInitializer : Initializer<BackupManager>, KoinComponent {
    private val manager by inject<BackupManager>()
    private val appLifecycleOwner by inject<LifecycleOwner>()

    override fun create(context: Context): BackupManager {
        manager.attach(appLifecycleOwner)

        return manager
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(KoinInitializer::class.java)
    }
}
