package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.exception.TitleEmptyException
import com.taetae98.diary.domain.repository.MemoFireStoreRepository
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class UpsertMemoUseCase internal constructor(
    private val memoRepository: MemoRepository,
    private val memoFireStoreRepository: MemoFireStoreRepository,
) : UseCase<Memo, Unit>() {
    override suspend fun execute(params: Memo) {
        checkTitle(params.title)
        memoRepository.upsert(params)
        memoFireStoreRepository.upsert(params)
    }

    private fun checkTitle(title: String) {
        if (title.isEmpty()) {
            throw TitleEmptyException()
        }
    }
}