package com.taetae98.diary.domain.usecase.app

import com.taetae98.diary.domain.usecase.core.UseCase
import com.taetae98.diary.domain.usecase.memo.FetchMemoUseCase
import com.taetae98.diary.domain.usecase.tag.FetchTagUseCase
import org.koin.core.annotation.Factory

@Factory
public class FetchDataUseCase internal constructor(
    private val fetchMemoUseCase: FetchMemoUseCase,
    private val fetchTagUseCase: FetchTagUseCase,
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        fetchMemoUseCase(params).getOrThrow()
        fetchTagUseCase(params).getOrThrow()
    }
}
