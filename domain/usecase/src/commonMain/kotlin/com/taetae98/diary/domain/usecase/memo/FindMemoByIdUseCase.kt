package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
public class FindMemoByIdUseCase(
    private val memoRepository: MemoRepository,
) : FlowUseCase<String, Memo?>() {
    override fun execute(params: String): Flow<Result<Memo?>> {
        return memoRepository
            .find(params)
            .map { Result.success(it) }
    }
}
