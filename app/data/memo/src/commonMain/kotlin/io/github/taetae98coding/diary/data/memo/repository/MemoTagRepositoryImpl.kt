package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.diary.database.MemoTagDao
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoTagRepositoryImpl(private val localDataSource: MemoTagDao) : MemoTagRepository {
	override fun findTagIdsByMemoId(memoId: String): Flow<Set<String>> = localDataSource.findTagIdsByMemoId(memoId)

	override suspend fun upsert(memoId: String, tagId: String) {
		localDataSource.upsert(memoId, tagId)
	}

	override suspend fun delete(memoId: String, tagId: String) {
		localDataSource.delete(memoId, tagId)
	}
}
