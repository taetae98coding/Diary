package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagFireStoreRepository
import com.taetae98.diary.domain.repository.MemoTagRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.UseCase
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class DeleteMemoTagUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val memoTagRepository: MemoTagRepository,
    private val memoTagFireStoreRepository: MemoTagFireStoreRepository,
) : UseCase<MemoTag, Unit>() {
    override suspend fun execute(params: MemoTag) {
        if (params.isInvalid()) return

        deleteFireStore(params)
        memoTagRepository.delete(params)
    }

    private suspend fun deleteFireStore(memoTag: MemoTag) {
        if (getAccountUseCase(Unit).first().getOrThrow().isLogin) {
            memoTagFireStoreRepository.delete(memoTag)
        }
    }
}
