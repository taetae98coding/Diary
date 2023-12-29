package com.taetae98.diary.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.app.FetchDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

internal class FetchDataService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    private val getAccountUseCase by inject<GetAccountUseCase>()
    private val fetchDataUseCase by inject<FetchDataUseCase>()

    private var job: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val currentJob = job

        if (currentJob == null || currentJob.isCompleted) {
            job = serviceScope.launch {
                execute()
            }
        }

        return START_NOT_STICKY
    }

    private suspend fun execute() {
        getAccountUseCase(Unit).mapLatest { it.getOrNull() }
            .mapLatest { it?.uid }
            .filterNotNull()
            .collectLatest(::onUidChanged)
    }

    private suspend fun onUidChanged(uid: String) {
        fetchDataUseCase(uid)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}