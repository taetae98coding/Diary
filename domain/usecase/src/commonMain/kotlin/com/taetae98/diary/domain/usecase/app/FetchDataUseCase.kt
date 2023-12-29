package com.taetae98.diary.domain.usecase.app

import com.taetae98.diary.domain.entity.account.account.Account
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.UseCase
import com.taetae98.diary.domain.usecase.memo.FetchMemoUseCase
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.annotation.Factory

@Factory
public class FetchDataUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val fetchMemoUseCase: FetchMemoUseCase,
) : UseCase<Unit, Unit>() {
    override suspend fun execute(params: Unit) {
        getAccountUseCase(Unit).collectLatest(::fetchData)
    }

    private suspend fun fetchData(result: Result<Account>) {
        val uid = result.getOrNull()?.uid ?: return

        fetchMemoUseCase(uid)
    }
}
