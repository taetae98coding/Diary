package com.taetae98.diary.app.worker

import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.app.FetchDataUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
public class FetchDataWorker(
    @Named(CoroutinesModule.MAIN)
    dispatcher: CoroutineDispatcher,
    private val getAccountUseCase: GetAccountUseCase,
    private val fetchDataUseCase: FetchDataUseCase,
) {
    private val scope = CoroutineScope(dispatcher + SupervisorJob())
    private var job: Job? = null

    public fun execute() {
        if (job?.isCompleted == null || job?.isCompleted == true) {
            job = scope.launch {
                observeAccount()
            }
        }
    }

    public fun cancel() {
        job?.cancel()
    }

    private suspend fun observeAccount() {
        getAccountUseCase(Unit).mapLatest { it.getOrNull() }
            .mapLatest { it?.uid }
            .filterNotNull()
            .collectLatest(::onUidChanged)
    }

    private suspend fun onUidChanged(uid: String) {
        fetchDataUseCase(uid)
    }
}