package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

@Factory
public class FindMemoTagByMemoIdUseCase(
    private val memoTagRepository: MemoTagRepository,
) : FlowUseCase<String?, List<MemoTag>>() {
    override fun execute(params: String?): Flow<List<MemoTag>> {
        return if (params == null) {
            flowOf(emptyList())
        } else {
            memoTagRepository.findByMemoId(params)
        }
    }
}