package io.github.taetae98coding.diary.core.diary.database.room.dao

import io.github.taetae98coding.diary.core.diary.database.MemoTagDao
import io.github.taetae98coding.diary.core.diary.database.room.DiaryDatabase
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoTagEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class MemoTagRoomDao(private val database: DiaryDatabase) : MemoTagDao {
	override fun findTagIdsByMemoId(memoId: String): Flow<Set<String>> =
		database
			.memoTag()
			.findTagIdsByMemoId(memoId)
			.mapLatest(List<String>::toSet)

	override suspend fun upsert(memoId: String, tagId: String) {
		database.memoTag().upsert(MemoTagEntity(memoId, tagId))
	}

	override suspend fun delete(memoId: String, tagId: String) {
		database.memoTag().delete(MemoTagEntity(memoId, tagId))
	}
}
