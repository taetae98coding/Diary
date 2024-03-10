package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.repository.MemoFireStoreRepository
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.UseCase
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpdateMemoFinishUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val memoRepository: MemoRepository,
    private val memoFireStoreRepository: MemoFireStoreRepository,
) : UseCase<UpdateMemoFinishUseCase.Params, Unit>() {
    override suspend fun execute(params: Params) {
        if (params.isInvalid()) return

        memoRepository.updateFinish(params.memoId, params.isFinish)
        updateFinishFireStore(params)
    }

    private suspend fun updateFinishFireStore(params: Params) {
        if (getAccountUseCase(Unit).first().getOrThrow().isLogin) {
            memoFireStoreRepository.updateFinish(params.memoId, params.isFinish)
        }
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