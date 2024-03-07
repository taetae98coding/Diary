package com.taetae98.diary.data.repository.memo.tag

import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.data.local.api.MemoTagLocalDataSource
import com.taetae98.diary.data.pref.api.MemoTagPrefDataSource
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagRepository
import com.taetae98.diary.library.kotlin.ext.mapCollectionLatest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class MemoTagRepositoryImpl(
    private val fireStore: MemoTagFireStore,
    private val prefDataSource: MemoTagPrefDataSource,
    private val localDataSource: MemoTagLocalDataSource,
    @Named(CoroutinesModule.PROCESS)
    private val processScope: CoroutineScope,
) : MemoTagRepository {
    override suspend fun delete(memoTag: MemoTag) {
        val dto = memoTag.toDto()

        localDataSource.delete(dto)
        processScope.launch { fireStore.delete(dto) }
    }

    override suspend fun upsert(memoTag: MemoTag) {
        val dto = memoTag.toDto()

        localDataSource.upsert(dto)
        processScope.launch { fireStore.upsert(dto) }
    }

    override suspend fun upsert(memoTag: List<MemoTag>) {
        val dto = memoTag.map(MemoTag::toDto)

        localDataSource.upsert(dto)
        processScope.launch { dto.forEach { fireStore.upsert(it) } }
    }

    override fun findByMemoId(memoId: String): Flow<List<MemoTag>> {
        return localDataSource.findByMemoId(memoId)
            .mapCollectionLatest(MemoTagDto::toDomain)
    }

    override suspend fun fetch(uid: String) {
        while (true) {
            val updateAt = prefDataSource.getFetchedUpdateAt(uid).firstOrNull()
            val memoList = localDataSource.afterAt(uid, updateAt, 50L).takeIf { it.isNotEmpty() } ?: break

            memoList.flatMap { fireStore.getMemoTagList(it.id) }
                .forEach {
                    if (it.isDeleted) {
                        localDataSource.delete(it)
                    } else {
                        localDataSource.upsert(it)
                    }
                }

            prefDataSource.setFetchedUpdateAt(uid, memoList.last().updateAt)
        }
    }
}
