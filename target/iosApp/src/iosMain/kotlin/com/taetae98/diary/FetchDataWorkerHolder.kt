package com.taetae98.diary

import com.taetae98.diary.app.worker.FetchDataWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

public class FetchDataWorkerHolder : KoinComponent {
    private val worker by inject<FetchDataWorker>()

    public fun execute() {
        worker.execute()
    }

    public fun cancel() {
        worker.cancel()
    }
}