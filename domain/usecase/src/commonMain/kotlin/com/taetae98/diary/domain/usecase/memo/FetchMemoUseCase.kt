package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.repository.MemoFireStoreRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class FetchMemoUseCase internal constructor(
    private val memoFireStoreRepository: MemoFireStoreRepository,
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        memoFireStoreRepository.fetch(params)
    }
}
