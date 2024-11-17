package io.github.taetae98coding.diary.core.diary.database.room.dao

import io.github.taetae98coding.diary.core.diary.database.MemoTagDao
import io.github.taetae98coding.diary.core.diary.database.room.DiaryDatabase
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoTagEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.TagEntity
import io.github.taetae98coding.diary.core.diary.database.room.mapper.toDto
import io.github.taetae98coding.diary.core.model.tag.TagDto
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoTagRoomDao(
    private val database: DiaryDatabase,
) : MemoTagDao {
    override fun findByMemoId(memoId: String): Flow<List<TagDto>> {
        return database.memoTag().findTagByMemoId(memoId)
            .mapCollectionLatest(TagEntity::toDto)
    }

    override suspend fun upsert(memoId: String, tagId: String) {
        database.memoTag().upsert(MemoTagEntity(memoId, tagId))
    }

    override suspend fun delete(memoId: String, tagId: String) {
        database.memoTag().delete(MemoTagEntity(memoId, tagId))
    }
}
