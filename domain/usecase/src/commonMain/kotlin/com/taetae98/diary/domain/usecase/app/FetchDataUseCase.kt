package com.taetae98.diary.domain.usecase.app

import com.taetae98.diary.domain.usecase.core.UseCase
import com.taetae98.diary.domain.usecase.memo.FetchMemoUseCase
import org.koin.core.annotation.Factory

@Factory
public class FetchDataUseCase internal constructor(
    private val fetchMemoUseCase: FetchMemoUseCase,
) : UseCase<Unit, Unit>() {
    override suspend fun execute(params: Unit) {
        fetchMemoUseCase(Unit)
    }
}
