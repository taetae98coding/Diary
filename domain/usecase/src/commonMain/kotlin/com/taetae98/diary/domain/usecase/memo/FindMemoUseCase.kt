package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.account.memo.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class FindMemoUseCase(
    private val memoRepository: MemoRepository,
) : UseCase<String, Memo?>() {
    override suspend fun execute(params: String): Memo? {
        return memoRepository.find(params)
    }
}
