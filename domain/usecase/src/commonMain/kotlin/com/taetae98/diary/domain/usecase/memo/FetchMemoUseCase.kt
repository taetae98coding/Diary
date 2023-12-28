package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class FetchMemoUseCase internal constructor(
    private val memoRepository: MemoRepository,
) : UseCase<Unit, Unit>() {
    override suspend fun execute(params: Unit) {
        memoRepository.fetch()
    }
}
