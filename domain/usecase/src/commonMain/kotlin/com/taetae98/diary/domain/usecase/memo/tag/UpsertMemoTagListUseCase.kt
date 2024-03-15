package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagFireStoreRepository
import com.taetae98.diary.domain.repository.MemoTagRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.UseCase
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpsertMemoTagListUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val memoTagFireStoreRepository: MemoTagFireStoreRepository,
    private val memoTagRepository: MemoTagRepository,
) : UseCase<List<MemoTag>, Unit>() {
    override suspend fun execute(params: List<MemoTag>) {
        upsertFireStore(params)
        memoTagRepository.upsert(params)
    }

    private suspend fun upsertFireStore(memoTag: List<MemoTag>) {
        if (getAccountUseCase(Unit).first().getOrThrow().isLogin) {
            memoTagFireStoreRepository.upsert(memoTag)
        }
    }
}
