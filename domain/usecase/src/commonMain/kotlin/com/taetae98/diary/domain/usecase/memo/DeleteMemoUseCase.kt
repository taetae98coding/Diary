package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.memo.MemoId
import com.taetae98.diary.domain.repository.MemoFireStoreRepository
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.UseCase
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class DeleteMemoUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val memoRepository: MemoRepository,
    private val memoFireStoreRepository: MemoFireStoreRepository,
) : UseCase<MemoId, Memo?>() {
    override suspend fun execute(params: MemoId): Memo? {
        if (params.isInvalid()) return null

        deleteFireStore(params)
        return memoRepository.delete(params.value)
    }

    private suspend fun deleteFireStore(memoId: MemoId) {
        if (getAccountUseCase(Unit).first().getOrThrow().isLogin) {
            memoFireStoreRepository.delete(memoId.value)
        }
    }
}
