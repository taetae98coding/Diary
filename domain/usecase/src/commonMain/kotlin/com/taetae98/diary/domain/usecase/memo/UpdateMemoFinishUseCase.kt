package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class UpdateMemoFinishUseCase internal constructor(
    private val memoRepository: MemoRepository,
) : UseCase<UpdateMemoFinishUseCase.Params, Unit>() {
    override suspend fun execute(params: Params) {
        if (params.isInvalid()) return

        memoRepository.updateFinish(params.memoId, params.isFinish)
    }

    public data class Params(
        val memoId: String,
        val isFinish: Boolean,
    ) {
        public fun isInvalid(): Boolean {
            return memoId.isEmpty()
        }
    }
}