package io.github.taetae98coding.diary.core.diary.database.room.dao

import io.github.taetae98coding.diary.core.diary.database.TagMemoDao
import io.github.taetae98coding.diary.core.diary.database.room.DiaryDatabase
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.mapper.toDto
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class TagMemoRoomDao(
	private val database: DiaryDatabase,
) : TagMemoDao {
	override fun pageMemoByTagId(tagId: String): Flow<List<MemoDto>> = database
		.memoTag()
		.pageMemoByTagId(tagId)
		.mapCollectionLatest(MemoEntity::toDto)
}
