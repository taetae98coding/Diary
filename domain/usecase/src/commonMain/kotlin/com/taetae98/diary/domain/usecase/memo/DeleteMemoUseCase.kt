package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Factory

@Factory
public class DeleteMemoUseCase internal constructor(
    private val memoRepository: MemoRepository,
) : UseCase<String, Memo?>() {
    override suspend fun execute(params: String): Memo? {
        val memo = memoRepository.find(params).firstOrNull()
        memoRepository.delete(params)

        return memo
    }
}
