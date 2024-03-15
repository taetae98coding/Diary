package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.repository.MemoTagFireStoreRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class FetchMemoTagUseCase internal constructor(
    private val memoTagFireStoreRepository: MemoTagFireStoreRepository
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        memoTagFireStoreRepository.fetch(params)
    }
}