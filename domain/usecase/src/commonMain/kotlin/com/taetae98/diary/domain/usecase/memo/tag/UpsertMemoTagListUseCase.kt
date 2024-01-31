package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class UpsertMemoTagListUseCase internal constructor(
    private val memoTagRepository: MemoTagRepository,
) : UseCase<List<MemoTag>, Unit>() {
    override suspend fun execute(params: List<MemoTag>) {
        memoTagRepository.upsert(params)
    }
}
