package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.memo.MemoId
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class DeleteMemoUseCase internal constructor(
    private val memoRepository: MemoRepository,
) : UseCase<MemoId, Memo?>() {
    override suspend fun execute(params: MemoId): Memo? {
        if (params.isInvalid()) return null

        return memoRepository.delete(params.value)
    }
}
