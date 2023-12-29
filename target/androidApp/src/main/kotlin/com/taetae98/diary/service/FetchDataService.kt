package com.taetae98.diary.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.taetae98.diary.app.worker.FetchDataWorker
import org.koin.android.ext.android.inject

internal class FetchDataService : Service() {
    private val worker by inject<FetchDataWorker>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        worker.execute()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        worker.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}