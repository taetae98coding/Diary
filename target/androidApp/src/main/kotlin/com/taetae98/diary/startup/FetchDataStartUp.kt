package com.taetae98.diary.startup

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.startup.Initializer
import com.taetae98.diary.domain.usecase.app.FetchDataUseCase
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class FetchDataStartUp : Initializer<Unit>, KoinComponent {
    override fun create(context: Context) {
        val fetchDataUseCase by inject<FetchDataUseCase>()

        ProcessLifecycleOwner.get().lifecycleScope.launch {
            ProcessLifecycleOwner.get().repeatOnLifecycle(Lifecycle.State.STARTED) {
                fetchDataUseCase(Unit)
            }
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(FirebaseStartup::class.java)
    }
}