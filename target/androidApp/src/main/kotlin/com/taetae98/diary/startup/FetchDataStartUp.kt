package com.taetae98.diary.startup

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.startup.Initializer
import com.taetae98.diary.service.FetchDataService
import kotlinx.coroutines.launch

internal class FetchDataStartUp : Initializer<Unit> {
    override fun create(context: Context) {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            ProcessLifecycleOwner.get().repeatOnLifecycle(Lifecycle.State.STARTED) {
                context.startService(Intent(context, FetchDataService::class.java))
            }
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(FirebaseStartup::class.java, KoinStartUp::class.java)
    }
}
