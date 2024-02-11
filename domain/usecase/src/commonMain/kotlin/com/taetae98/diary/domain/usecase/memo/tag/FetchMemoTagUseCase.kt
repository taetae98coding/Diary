package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.repository.MemoTagRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class FetchMemoTagUseCase internal constructor(
    private val memoTagRepository: MemoTagRepository
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        memoTagRepository.fetch(params)
    }
}