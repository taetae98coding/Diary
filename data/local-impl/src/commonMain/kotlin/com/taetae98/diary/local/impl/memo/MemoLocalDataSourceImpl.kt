package com.taetae98.diary.local.impl.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.local.api.MemoLocalDataSource
import org.koin.core.annotation.Factory

@Factory
internal class MemoLocalDataSourceImpl(
    private val memoDao: MemoDao
) : MemoLocalDataSource {
    override suspend fun upsert(memo: MemoDto) {
        memoDao.upsert(memo)
    }
}