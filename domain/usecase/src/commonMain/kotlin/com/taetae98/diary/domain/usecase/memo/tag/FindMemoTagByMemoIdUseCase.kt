package com.taetae98.diary.domain.usecase.memo.tag

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
) : FlowUseCase<String?, List<MemoTag>>() {
    override fun execute(params: String?): Flow<Result<List<MemoTag>>> {
        if (params == null) return flowOf(Result.success(emptyList()))

        return memoTagRepository
            .findByMemoId(params)
            .map { Result.success(it) }
    }
}