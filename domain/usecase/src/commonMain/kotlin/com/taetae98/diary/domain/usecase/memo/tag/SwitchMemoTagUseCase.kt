package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class SwitchMemoTagUseCase internal constructor(
    private val memoTagRepository: MemoTagRepository,
) : UseCase<MemoTag, Unit>() {
    override suspend fun execute(params: MemoTag) {
        if (memoTagRepository.exists(params)) {
            memoTagRepository.delete(params)
        } else {
            memoTagRepository.upsert(params)
        }
    }
}