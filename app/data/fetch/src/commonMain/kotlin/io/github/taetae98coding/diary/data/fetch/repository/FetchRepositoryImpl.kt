package io.github.taetae98coding.diary.data.fetch.repository

import io.github.taetae98coding.diary.core.diary.database.MemoDao
import io.github.taetae98coding.diary.core.diary.service.memo.MemoService
import io.github.taetae98coding.diary.domain.fetch.repository.FetchRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory

@Factory
internal class FetchRepositoryImpl(
    private val memoDao: MemoDao,
    private val memoService: MemoService,
) : FetchRepository {
    override suspend fun fetchMemo(uid: String) {
        while (true) {
            val updateAt = memoDao.getLastServerUpdateAt(uid).first() ?: Instant.fromEpochMilliseconds(0L)
            val memoList = memoService.fetch(updateAt)

            if (memoList.isEmpty()) {
                break
            }

            memoDao.upsert(memoList)
            delay(2000L)
        }
    }
}
