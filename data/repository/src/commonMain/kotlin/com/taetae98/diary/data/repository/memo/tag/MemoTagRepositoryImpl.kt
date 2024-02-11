package com.taetae98.diary.data.repository.memo.tag

import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.data.local.api.MemoTagLocalDataSource
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
import com.taetae98.diary.library.kotlin.ext.mapCollectionLatest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class MemoTagRepositoryImpl(
    private val localDataSource: MemoTagLocalDataSource,
    private val fireStore: MemoTagFireStore,
    @Named(CoroutinesModule.PROCESS)
    private val processScope: CoroutineScope
) : MemoTagRepository {
    override suspend fun exists(memoTag: MemoTag): Boolean {
        return localDataSource.exists(memoTag.toDto())
    }

    override suspend fun delete(memoTag: MemoTag) {
        val dto = memoTag.toDto()

        localDataSource.delete(dto)
        processScope.launch { fireStore.delete(dto) }
    }

    override suspend fun upsert(memoTag: MemoTag) {
        val dto = memoTag.toDto()

        localDataSource.insert(dto)
        processScope.launch { fireStore.upsert(dto) }
    }

    override suspend fun upsert(memoTag: List<MemoTag>) {
        val dto = memoTag.map(MemoTag::toDto)

        localDataSource.insert(dto)
        processScope.launch { dto.forEach { fireStore.upsert(it) } }
    }

    override fun findByMemoId(memoId: String): Flow<List<MemoTag>> {
        return localDataSource.findByMemoId(memoId)
            .mapCollectionLatest(MemoTagDto::toDomain)
    }
}
