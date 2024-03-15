package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.entity.memo.MemoId
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
public class FindMemoTagByMemoIdUseCase internal constructor(
    private val memoTagRepository: MemoTagRepository,
) : FlowUseCase<MemoId, List<MemoTag>>() {
    override fun execute(params: MemoId): Flow<Result<List<MemoTag>>> {
        if (params.isInvalid()) return flowOf(Result.success(emptyList()))

        return memoTagRepository
            .findByMemoId(params.value)
            .map { Result.success(it) }
    }
}