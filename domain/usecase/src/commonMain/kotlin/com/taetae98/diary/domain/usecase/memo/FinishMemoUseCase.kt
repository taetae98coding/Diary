package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class FinishMemoUseCase internal constructor(
    private val memoRepository: MemoRepository,
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        memoRepository.delete(params)
    }
}
